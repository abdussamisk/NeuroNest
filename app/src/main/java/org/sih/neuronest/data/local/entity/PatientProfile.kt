package org.sih.neuronest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patient_profile")
data class PatientProfile(
    @PrimaryKey val id: String = "default_patient",
    val name: String = "Patient User",
    val age: Int = 72,
    val selectedRegion: String = "ASSAM", // ASSAM, MANIPUR, NAGALAND, MEGHALAYA, etc.
    val preferredLanguage: String = "Assamese", // Assamese, Bengali, Manipuri, English, Hindi
    val currentDifficultyLevel: Int = 2, // 1 to 5
    val cognitiveHealthIndex: Float = 84.5f, // 0 to 100
    val baselineScore: Float = 82.0f,
    val caregiverContactPhone: String = "+91 98765 43210",
    val voicePromptsEnabled: Boolean = true
)
