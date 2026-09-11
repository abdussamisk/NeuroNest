package org.sih.neuronest.data.remote.supabase.dto

import com.google.gson.annotations.SerializedName
import org.sih.neuronest.data.local.entity.PatientProfile

data class PatientProfileDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("age") val age: Int,
    @SerializedName("selected_region") val selectedRegion: String,
    @SerializedName("preferred_language") val preferredLanguage: String,
    @SerializedName("current_difficulty_level") val currentDifficultyLevel: Int,
    @SerializedName("cognitive_health_index") val cognitiveHealthIndex: Float,
    @SerializedName("baseline_score") val baselineScore: Float,
    @SerializedName("caregiver_contact_phone") val caregiverContactPhone: String,
    @SerializedName("voice_prompts_enabled") val voicePromptsEnabled: Boolean
) {
    companion object {
        fun fromEntity(entity: PatientProfile): PatientProfileDto {
            return PatientProfileDto(
                id = entity.id,
                name = entity.name,
                age = entity.age,
                selectedRegion = entity.selectedRegion,
                preferredLanguage = entity.preferredLanguage,
                currentDifficultyLevel = entity.currentDifficultyLevel,
                cognitiveHealthIndex = entity.cognitiveHealthIndex,
                baselineScore = entity.baselineScore,
                caregiverContactPhone = entity.caregiverContactPhone,
                voicePromptsEnabled = entity.voicePromptsEnabled
            )
        }
    }

    fun toEntity(): PatientProfile {
        return PatientProfile(
            id = id,
            name = name,
            age = age,
            selectedRegion = selectedRegion,
            preferredLanguage = preferredLanguage,
            currentDifficultyLevel = currentDifficultyLevel,
            cognitiveHealthIndex = cognitiveHealthIndex,
            baselineScore = baselineScore,
            caregiverContactPhone = caregiverContactPhone,
            voicePromptsEnabled = voicePromptsEnabled
        )
    }
}
