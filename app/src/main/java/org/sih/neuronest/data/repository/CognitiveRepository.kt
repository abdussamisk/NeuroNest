package org.sih.neuronest.data.repository

import kotlinx.coroutines.flow.Flow
import org.sih.neuronest.data.local.dao.AlertDao
import org.sih.neuronest.data.local.dao.CognitiveDao
import org.sih.neuronest.data.local.entity.CaregiverAlert
import org.sih.neuronest.data.local.entity.CognitiveSession
import org.sih.neuronest.data.local.entity.PatientProfile
import org.sih.neuronest.domain.ai.CognitiveDifficultyEngine
import org.sih.neuronest.domain.ai.TFLiteAnomalyDetector
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CognitiveRepository @Inject constructor(
    private val cognitiveDao: CognitiveDao,
    private val alertDao: AlertDao,
    private val difficultyEngine: CognitiveDifficultyEngine,
    private val tfliteAnomalyDetector: TFLiteAnomalyDetector
) {

    val patientProfileFlow: Flow<PatientProfile?> = cognitiveDao.getPatientProfileFlow()
    val allSessionsFlow: Flow<List<CognitiveSession>> = cognitiveDao.getAllSessionsFlow()
    val allAlertsFlow: Flow<List<CaregiverAlert>> = alertDao.getAllAlertsFlow()

    suspend fun getOrCreatePatientProfile(): PatientProfile {
        var profile = cognitiveDao.getPatientProfileDirect()
        if (profile == null) {
            profile = PatientProfile()
            cognitiveDao.insertOrUpdateProfile(profile)
        }
        return profile
    }

    suspend fun recordGameSession(
        gameType: String,
        score: Int,
        totalAttempts: Int,
        successfulAttempts: Int,
        avgReactionTimeMs: Long
    ) {
        val currentProfile = getOrCreatePatientProfile()
        val accuracy = if (totalAttempts > 0) (successfulAttempts.toFloat() / totalAttempts.toFloat()) * 100f else 100f

        val evalResult = difficultyEngine.evaluatePerformance(
            currentLevel = currentProfile.currentDifficultyLevel,
            accuracyPercentage = accuracy,
            avgReactionTimeMs = avgReactionTimeMs,
            baselineScore = currentProfile.baselineScore
        )

        // Save session locally in Room DB
        val session = CognitiveSession(
            gameType = gameType,
            difficultyLevel = currentProfile.currentDifficultyLevel,
            score = score,
            totalAttempts = totalAttempts,
            successfulAttempts = successfulAttempts,
            averageReactionTimeMs = avgReactionTimeMs,
            accuracyPercentage = accuracy,
            isBaselineDipDetected = evalResult.isBaselineAnomalyDetected,
            isSyncedToCloud = false
        )
        cognitiveDao.insertSession(session)

        // Check if baseline anomaly alert needs to be generated for caregiver
        if (evalResult.isBaselineAnomalyDetected) {
            val alert = CaregiverAlert(
                alertType = "COGNITIVE_DIP",
                severity = "HIGH",
                title = "Cognitive Baseline Dip Alert",
                description = "Patient showed a drop in accuracy (${accuracy.toInt()}%) during $gameType. Difficulty adjusted to Level ${evalResult.recommendedLevel}."
            )
            alertDao.insertAlert(alert)
        }

        // Update patient profile with new health index & level
        val updatedHealthIndex = (currentProfile.cognitiveHealthIndex + evalResult.healthIndexAdjustment).coerceIn(0f, 100f)
        val updatedProfile = currentProfile.copy(
            cognitiveHealthIndex = updatedHealthIndex,
            currentDifficultyLevel = evalResult.recommendedLevel
        )
        cognitiveDao.insertOrUpdateProfile(updatedProfile)
    }

    suspend fun updateRegion(regionName: String) {
        val profile = getOrCreatePatientProfile()
        cognitiveDao.insertOrUpdateProfile(profile.copy(selectedRegion = regionName))
    }

    suspend fun dismissAlert(alertId: String) {
        alertDao.dismissAlert(alertId)
    }
}
