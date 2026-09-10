package org.sih.neuronest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "routine_reminder")
data class RoutineReminder(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val category: String, // MEDICINE, HYDRATION, MEAL, WALK, PRAYER
    val scheduledTimeFormatted: String, // e.g. "09:00 AM"
    val audioPromptText: String,
    val isAcknowledged: Boolean = false,
    val isMissed: Boolean = false,
    val visualCueIcon: String = "ic_medicine"
)
