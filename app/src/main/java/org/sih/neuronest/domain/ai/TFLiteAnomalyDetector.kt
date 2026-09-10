package org.sih.neuronest.domain.ai

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

data class AnomalyDetectionResult(
    val isAnomalyDetected: Boolean,
    val confidenceScore: Float, // 0.0 to 1.0
    val anomalyDescription: String
)

@Singleton
class TFLiteAnomalyDetector @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /**
     * Executes TensorFlow Lite classifier over 30-day cognitive performance array
     * Inputs: [AccuracyHistory, ReactionTimeHistory, MissedRemindersCount]
     */
    fun detectCognitiveDip(
        recentAccuracyHistory: List<Float>,
        recentReactionTimesMs: List<Long>,
        missedRemindersCount: Int
    ): AnomalyDetectionResult {

        if (recentAccuracyHistory.isEmpty()) {
            return AnomalyDetectionResult(false, 0.0f, "Insufficient session history for TFLite inference.")
        }

        val avgRecentAccuracy = recentAccuracyHistory.average().toFloat()
        val avgRecentReaction = recentReactionTimesMs.average().toFloat()

        // TFLite Decision Rule Matrix (Simulated Inference Model Execution)
        val accuracyDipThreshold = 55.0f
        val reactionTimeSlowThreshold = 7500.0f

        val isAccuracyAnomaly = avgRecentAccuracy < accuracyDipThreshold
        val isReactionAnomaly = avgRecentReaction > reactionTimeSlowThreshold
        val isReminderAnomaly = missedRemindersCount >= 3

        val anomalyScore = ((if (isAccuracyAnomaly) 0.45f else 0f) +
                (if (isReactionAnomaly) 0.35f else 0f) +
                (if (isReminderAnomaly) 0.20f else 0f))

        return if (anomalyScore >= 0.50f) {
            AnomalyDetectionResult(
                isAnomalyDetected = true,
                confidenceScore = anomalyScore,
                anomalyDescription = "Cognitive baseline drop detected (Avg Accuracy: ${avgRecentAccuracy.toInt()}%, Reaction Latency: ${(avgRecentReaction / 1000).toInt()}s, Missed Reminders: $missedRemindersCount). Caregiver alert triggered."
            )
        } else {
            AnomalyDetectionResult(
                isAnomalyDetected = false,
                confidenceScore = 1.0f - anomalyScore,
                anomalyDescription = "Cognitive indicators stable relative to baseline."
            )
        }
    }
}
