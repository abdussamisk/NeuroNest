package org.sih.neuronest.presentation.patient.games

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sih.neuronest.data.repository.CognitiveRepository
import org.sih.neuronest.domain.ai.VoiceAssistantEngine
import javax.inject.Inject

data class TargetFocusItem(
    val id: Int,
    val emoji: String,
    val isTarget: Boolean
)

data class AttentionFocusUiState(
    val activeIndex: Int = 0,
    val items: List<TargetFocusItem> = emptyList(),
    val targetEmoji: String = "🦏",
    val score: Int = 0,
    val totalTaps: Int = 0,
    val successfulTaps: Int = 0,
    val isCompleted: Boolean = false,
    val feedbackText: String = "Tap the Kaziranga Rhino when it appears!"
)

@HiltViewModel
class AttentionFocusViewModel @Inject constructor(
    private val cognitiveRepository: CognitiveRepository,
    private val voiceAssistantEngine: VoiceAssistantEngine
) : ViewModel() {

    private val _uiState = MutableStateFlow(AttentionFocusUiState())
    val uiState: StateFlow<AttentionFocusUiState> = _uiState.asStateFlow()

    private var startTimeMs: Long = System.currentTimeMillis()

    init {
        startExercise()
    }

    fun startExercise() {
        val emojis = listOf("🦏", "🌸", "🍃", "🐘", "🦚")
        val target = "🦏"

        _uiState.value = AttentionFocusUiState(
            targetEmoji = target,
            score = 0,
            totalTaps = 0,
            successfulTaps = 0,
            isCompleted = false
        )
        startTimeMs = System.currentTimeMillis()
        voiceAssistantEngine.speakInstruction("Attention exercise! Tap the Rhino whenever it appears on screen.")

        // Start cycling targets
        viewModelScope.launch {
            for (round in 1..8) {
                if (_uiState.value.isCompleted) break
                val currentEmoji = emojis.random()
                val isTarget = currentEmoji == target
                _uiState.value = _uiState.value.copy(
                    items = listOf(TargetFocusItem(id = round, emoji = currentEmoji, isTarget = isTarget))
                )
                delay(1800)
            }
            finishExercise()
        }
    }

    fun onItemTap(item: TargetFocusItem) {
        if (_uiState.value.isCompleted) return

        val total = _uiState.value.totalTaps + 1
        if (item.isTarget) {
            val success = _uiState.value.successfulTaps + 1
            val newScore = _uiState.value.score + 20
            _uiState.value = _uiState.value.copy(
                score = newScore,
                totalTaps = total,
                successfulTaps = success,
                feedbackText = "Great focus! Target tapped!"
            )
        } else {
            _uiState.value = _uiState.value.copy(
                totalTaps = total,
                feedbackText = "Not the target. Keep watching!"
            )
        }
    }

    private fun finishExercise() {
        _uiState.value = _uiState.value.copy(
            isCompleted = true,
            feedbackText = "Exercise finished! Focused attention session complete."
        )
        val reactionTime = System.currentTimeMillis() - startTimeMs
        val success = _uiState.value.successfulTaps
        val total = _uiState.value.totalTaps.coerceAtLeast(1)

        viewModelScope.launch {
            cognitiveRepository.recordGameSession(
                gameType = "ATTENTION_FOCUS",
                score = _uiState.value.score,
                totalAttempts = total,
                successfulAttempts = success,
                avgReactionTimeMs = reactionTime / total
            )
        }
    }
}
