package org.sih.neuronest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "cognitive_session")
data class CognitiveSession(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val gameType: String, // MEMORY_MATCH, SEQUENCE_RECALL, PATTERN_RECOGNITION, etc.
    val difficultyLevel: Int,
    val score: Int,
    val totalAttempts: Int,
    val successfulAttempts: Int,
    val averageReactionTimeMs: Long,
    val accuracyPercentage: Float,
    val isBaselineDipDetected: Boolean = false,
    val isSyncedToCloud: Boolean = false
)
