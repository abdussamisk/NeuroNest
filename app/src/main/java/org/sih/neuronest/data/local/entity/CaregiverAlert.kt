package org.sih.neuronest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "caregiver_alert")
data class CaregiverAlert(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val alertType: String, // COGNITIVE_DIP, MISSED_MEDICATION, HIGH_REACTION_TIME
    val severity: String = "MEDIUM", // LOW, MEDIUM, HIGH, CRITICAL
    val title: String,
    val description: String,
    val isDismissed: Boolean = false
)
