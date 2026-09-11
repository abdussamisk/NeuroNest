package org.sih.neuronest.data.remote.supabase.dto

import com.google.gson.annotations.SerializedName
import org.sih.neuronest.data.local.entity.CognitiveSession

data class CognitiveSessionDto(
    @SerializedName("id") val id: String,
    @SerializedName("timestamp") val timestamp: Long,
    @SerializedName("game_type") val gameType: String,
    @SerializedName("difficulty_level") val difficultyLevel: Int,
    @SerializedName("score") val score: Int,
    @SerializedName("total_attempts") val totalAttempts: Int,
    @SerializedName("successful_attempts") val successfulAttempts: Int,
    @SerializedName("average_reaction_time_ms") val averageReactionTimeMs: Long,
    @SerializedName("accuracy_percentage") val accuracyPercentage: Float,
    @SerializedName("is_baseline_dip_detected") val isBaselineDipDetected: Boolean
) {
    companion object {
        fun fromEntity(entity: CognitiveSession): CognitiveSessionDto {
            return CognitiveSessionDto(
                id = entity.id,
                timestamp = entity.timestamp,
                gameType = entity.gameType,
                difficultyLevel = entity.difficultyLevel,
                score = entity.score,
                totalAttempts = entity.totalAttempts,
                successfulAttempts = entity.successfulAttempts,
                averageReactionTimeMs = entity.averageReactionTimeMs,
                accuracyPercentage = entity.accuracyPercentage,
                isBaselineDipDetected = entity.isBaselineDipDetected
            )
        }
    }

    fun toEntity(): CognitiveSession {
        return CognitiveSession(
            id = id,
            timestamp = timestamp,
            gameType = gameType,
            difficultyLevel = difficultyLevel,
            score = score,
            totalAttempts = totalAttempts,
            successfulAttempts = successfulAttempts,
            averageReactionTimeMs = averageReactionTimeMs,
            accuracyPercentage = accuracyPercentage,
            isBaselineDipDetected = isBaselineDipDetected,
            isSyncedToCloud = true
        )
    }
}
