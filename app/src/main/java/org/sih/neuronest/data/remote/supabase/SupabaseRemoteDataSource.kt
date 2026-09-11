package org.sih.neuronest.data.remote.supabase

import org.sih.neuronest.data.local.entity.CaregiverAlert
import org.sih.neuronest.data.local.entity.CognitiveSession
import org.sih.neuronest.data.local.entity.PatientProfile
import org.sih.neuronest.data.remote.supabase.dto.CaregiverAlertDto
import org.sih.neuronest.data.remote.supabase.dto.CognitiveSessionDto
import org.sih.neuronest.data.remote.supabase.dto.PatientProfileDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupabaseRemoteDataSource @Inject constructor(
    private val apiService: SupabaseApiService
) {

    suspend fun syncCognitiveSessions(sessions: List<CognitiveSession>): Boolean {
        if (sessions.isEmpty()) return true
        return try {
            val dtos = sessions.map { CognitiveSessionDto.fromEntity(it) }
            val response = apiService.insertCognitiveSessions(
                headers = SupabaseConfig.getHeaders(),
                sessions = dtos
            )
            response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun syncPatientProfile(profile: PatientProfile): Boolean {
        return try {
            val dto = PatientProfileDto.fromEntity(profile)
            val response = apiService.upsertPatientProfile(
                headers = SupabaseConfig.getHeaders(),
                profile = listOf(dto)
            )
            response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun syncCaregiverAlerts(alerts: List<CaregiverAlert>): Boolean {
        if (alerts.isEmpty()) return true
        return try {
            val dtos = alerts.map { CaregiverAlertDto.fromEntity(it) }
            val response = apiService.insertCaregiverAlerts(
                headers = SupabaseConfig.getHeaders(),
                alerts = dtos
            )
            response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
