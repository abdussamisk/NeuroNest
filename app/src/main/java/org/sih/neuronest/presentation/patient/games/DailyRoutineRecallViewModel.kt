package org.sih.neuronest.presentation.patient.games

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sih.neuronest.data.repository.CognitiveRepository
import org.sih.neuronest.domain.ai.VoiceAssistantEngine
import javax.inject.Inject

data class RoutineItemCard(
    val timeSlot: String,
    val activityTitle: String,
    val iconEmoji: String,
    val correctOrder: Int
)

data class DailyRoutineUiState(
    val selectedItems: List<RoutineItemCard> = emptyList(),
    val availableItems: List<RoutineItemCard> = emptyList(),
    val score: Int = 0,
    val totalAttempts: Int = 0,
    val isCompleted: Boolean = false,
    val feedbackText: String = "Tap daily activities in correct morning-to-night order!"
)

@HiltViewModel
class DailyRoutineRecallViewModel @Inject constructor(
    private val cognitiveRepository: CognitiveRepository,
    private val voiceAssistantEngine: VoiceAssistantEngine
) : ViewModel() {

    private val _uiState = MutableStateFlow(DailyRoutineUiState())
    val uiState: StateFlow<DailyRoutineUiState> = _uiState.asStateFlow()

    private var startTimeMs: Long = System.currentTimeMillis()

    init {
        loadRoutineGame()
    }

    fun loadRoutineGame() {
        val fullRoutine = listOf(
            RoutineItemCard("08:00 AM", "Morning Tea & Breakfast 🍵", "🍵", 1),
            RoutineItemCard("09:00 AM", "Morning BP Medicine 💊", "💊", 2),
            RoutineItemCard("01:00 PM", "Nutritious Lunch 🍱", "🍱", 3),
            RoutineItemCard("05:00 PM", "Evening Park Walk 🚶‍♂️", "🚶‍♂️", 4)
        )

        _uiState.value = DailyRoutineUiState(
            selectedItems = emptyList(),
            availableItems = fullRoutine.shuffled(),
            score = 0,
            totalAttempts = 0,
            isCompleted = false
        )
        startTimeMs = System.currentTimeMillis()
        voiceAssistantEngine.speakInstruction("Daily routine recall! Tap the activities in order from morning to evening.")
    }

    fun onSelectActivity(item: RoutineItemCard) {
        val state = _uiState.value
        if (state.isCompleted) return

        val nextExpectedOrder = state.selectedItems.size + 1
        val attempts = state.totalAttempts + 1

        if (item.correctOrder == nextExpectedOrder) {
            val updatedSelected = state.selectedItems + item
            val updatedAvailable = state.availableItems.filter { it.correctOrder != item.correctOrder }
            val newScore = state.score + 25

            val isDone = updatedSelected.size == 4

            _uiState.value = state.copy(
                selectedItems = updatedSelected,
                availableItems = updatedAvailable,
                score = newScore,
                totalAttempts = attempts,
                isCompleted = isDone,
                feedbackText = if (isDone) "Perfect schedule! Routine exercise complete." else "Correct order! Tap the next activity."
            )

            if (isDone) {
                val reactionTime = System.currentTimeMillis() - startTimeMs
                viewModelScope.launch {
                    cognitiveRepository.recordGameSession(
                        gameType = "DAILY_ROUTINE_RECALL",
                        score = newScore,
                        totalAttempts = attempts,
                        successfulAttempts = 4,
                        avgReactionTimeMs = reactionTime / 4
                    )
                }
                voiceAssistantEngine.speakInstruction("Great job reconstructing your daily routine schedule!")
            }
        } else {
            _uiState.value = state.copy(
                totalAttempts = attempts,
                feedbackText = "Not quite. What activity comes earlier in the day?"
            )
        }
    }
}
