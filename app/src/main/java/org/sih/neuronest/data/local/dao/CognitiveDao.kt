package org.sih.neuronest.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.sih.neuronest.data.local.entity.CognitiveSession
import org.sih.neuronest.data.local.entity.PatientProfile

@Dao
interface CognitiveDao {

    @Query("SELECT * FROM patient_profile WHERE id = 'default_patient'")
    fun getPatientProfileFlow(): Flow<PatientProfile?>

    @Query("SELECT * FROM patient_profile WHERE id = 'default_patient'")
    suspend fun getPatientProfileDirect(): PatientProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: PatientProfile)

    @Query("SELECT * FROM cognitive_session ORDER BY timestamp DESC")
    fun getAllSessionsFlow(): Flow<List<CognitiveSession>>

    @Query("SELECT * FROM cognitive_session WHERE isSyncedToCloud = 0")
    suspend fun getUnsyncedSessions(): List<CognitiveSession>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: CognitiveSession)

    @Query("UPDATE cognitive_session SET isSyncedToCloud = 1 WHERE id = :sessionId")
    suspend fun markSessionSynced(sessionId: String)

    @Query("SELECT AVG(accuracyPercentage) FROM cognitive_session")
    fun getAverageAccuracyFlow(): Flow<Float?>

    @Query("SELECT AVG(averageReactionTimeMs) FROM cognitive_session")
    fun getAverageReactionTimeFlow(): Flow<Float?>
}
