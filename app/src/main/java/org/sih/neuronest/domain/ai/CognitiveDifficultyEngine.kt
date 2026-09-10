package org.sih.neuronest.domain.ai

import javax.inject.Inject
import javax.inject.Singleton

data class CognitiveDifficultyResult(
    val recommendedLevel: Int, // 1 to 5
    val healthIndexAdjustment: Float,
    val performanceFeedbackText: String,
    val isBaselineAnomalyDetected: Boolean
)

@Singleton
class CognitiveDifficultyEngine @Inject constructor() {

    /**
     * Dynamically adjusts game difficulty based on session performance:
     * - Accuracy (%)
     * - Average Reaction Time (ms)
     * - Total attempts vs successful attempts
     * - Patient's current level
     */
    fun evaluatePerformance(
        currentLevel: Int,
        accuracyPercentage: Float,
        avgReactionTimeMs: Long,
        baselineScore: Float
    ): CognitiveDifficultyResult {

        var recommendedLevel = currentLevel
        var healthIndexDelta = 0.0f
        var isAnomaly = false
        var feedback = "Great job completing your exercise!"

        // Evaluation Logic
        if (accuracyPercentage >= 85.0f && avgReactionTimeMs < 4000) {
            // High performance -> scale up difficulty gently if not at max
            if (currentLevel < 5) {
                recommendedLevel = currentLevel + 1
                feedback = "Outstanding performance! Moving to Level $recommendedLevel."
            } else {
                feedback = "Mastery achieved at Level 5!"
            }
            healthIndexDelta = +1.5f
        } else if (accuracyPercentage < 50.0f || avgReactionTimeMs > 9000) {
            // Struggling -> scale down difficulty to avoid frustration
            if (currentLevel > 1) {
                recommendedLevel = currentLevel - 1
                feedback = "Adjusting to Level $recommendedLevel for comforting practice."
            } else {
                feedback = "Keep practicing at Level 1. Take your time!"
            }
            healthIndexDelta = -2.0f

            // Baseline drop check
            if (accuracyPercentage < (baselineScore - 25.0f)) {
                isAnomaly = true
            }
        } else {
            feedback = "Consistent performance! Recommended Level $currentLevel."
            healthIndexDelta = +0.5f
        }

        return CognitiveDifficultyResult(
            recommendedLevel = recommendedLevel,
            healthIndexAdjustment = healthIndexDelta,
            performanceFeedbackText = feedback,
            isBaselineAnomalyDetected = isAnomaly
        )
    }
}
