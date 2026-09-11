package org.sih.neuronest.data.remote.supabase

import org.sih.neuronest.data.remote.supabase.dto.CaregiverAlertDto
import org.sih.neuronest.data.remote.supabase.dto.CognitiveSessionDto
import org.sih.neuronest.data.remote.supabase.dto.PatientProfileDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface SupabaseApiService {

    @POST("rest/v1/cognitive_sessions")
    suspend fun insertCognitiveSessions(
        @HeaderMap headers: Map<String, String>,
        @Body sessions: List<CognitiveSessionDto>
    ): Response<Unit>

    @GET("rest/v1/cognitive_sessions?select=*")
    suspend fun getCognitiveSessions(
        @HeaderMap headers: Map<String, String>
    ): Response<List<CognitiveSessionDto>>

    @POST("rest/v1/patient_profiles")
    suspend fun upsertPatientProfile(
        @HeaderMap headers: Map<String, String>,
        @Body profile: List<PatientProfileDto>
    ): Response<Unit>

    @GET("rest/v1/patient_profiles?select=*")
    suspend fun getPatientProfiles(
        @HeaderMap headers: Map<String, String>
    ): Response<List<PatientProfileDto>>

    @POST("rest/v1/caregiver_alerts")
    suspend fun insertCaregiverAlerts(
        @HeaderMap headers: Map<String, String>,
        @Body alerts: List<CaregiverAlertDto>
    ): Response<Unit>
}
