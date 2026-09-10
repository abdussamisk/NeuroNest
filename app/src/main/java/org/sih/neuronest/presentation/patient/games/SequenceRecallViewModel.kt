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

data class SequenceRecallUiState(
    val targetSequence: List<Int> = emptyList(),
    val playerInput: List<Int> = emptyList(),
    val activeHighlightedPad: Int? = null,
    val isDemonstrating: Boolean = false,
    val round: Int = 1,
    val score: Int = 0,
    val isGameOver: Boolean = false,
    val feedbackText: String = "Watch the Bihu drum sequence, then repeat it!"
)

@HiltViewModel
class SequenceRecallViewModel @Inject constructor(
    private val cognitiveRepository: CognitiveRepository,
    private val voiceAssistantEngine: VoiceAssistantEngine
) : ViewModel() {

    private val _uiState = MutableStateFlow(SequenceRecallUiState())
    val uiState: StateFlow<SequenceRecallUiState> = _uiState.asStateFlow()

    private var startTimeMs: Long = System.currentTimeMillis()
    private var totalAttempts = 0

    init {
        startNewGame()
    }

    fun startNewGame() {
        totalAttempts = 0
        startTimeMs = System.currentTimeMillis()
        val initialSeq = listOf((0..3).random(), (0..3).random())
        _uiState.value = SequenceRecallUiState(
            targetSequence = initialSeq,
            playerInput = emptyList(),
            round = 1,
            score = 0,
            isGameOver = false
        )
        demonstrateSequence(initialSeq)
    }

    private fun demonstrateSequence(sequence: List<Int>) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isDemonstrating = true,
                playerInput = emptyList(),
                feedbackText = "Listen to the Bihu drum rhythm..."
            )
            voiceAssistantEngine.speakInstruction("Watch and listen closely to the rhythm!")

            delay(1000)
            for (padIndex in sequence) {
                _uiState.value = _uiState.value.copy(activeHighlightedPad = padIndex)
                delay(600)
                _uiState.value = _uiState.value.copy(activeHighlightedPad = null)
                delay(300)
            }

            _uiState.value = _uiState.value.copy(
                isDemonstrating = false,
                feedbackText = "Your turn! Tap the drums in order."
            )
        }
    }

    fun onPadClick(padIndex: Int) {
        if (_uiState.value.isDemonstrating || _uiState.value.isGameOver) return

        val currentInput = _uiState.value.playerInput + padIndex
        val currentStep = currentInput.size - 1
        val target = _uiState.value.targetSequence

        if (currentInput[currentStep] == target[currentStep]) {
            // Correct step
            if (currentInput.size == target.size) {
                // Round Complete!
                totalAttempts++
                val newScore = _uiState.value.score + 25
                val nextRound = _uiState.value.round + 1

                if (nextRound > 4) {
                    // Game Finished Successfully
                    _uiState.value = _uiState.value.copy(
                        score = newScore,
                        isGameOver = true,
                        feedbackText = "Fantastic memory! All rhythm sequences completed!"
                    )
                    val reactionTime = System.currentTimeMillis() - startTimeMs
                    viewModelScope.launch {
                        cognitiveRepository.recordGameSession(
                            gameType = "SEQUENCE_RECALL",
                            score = newScore,
                            totalAttempts = totalAttempts,
                            successfulAttempts = totalAttempts,
                            avgReactionTimeMs = reactionTime / 4
                        )
                    }
                    voiceAssistantEngine.speakInstruction("Excellent! You completed all Bihu drum sequences!")
                } else {
                    val nextSeq = target + (0..3).random()
                    _uiState.value = _uiState.value.copy(
                        score = newScore,
                        round = nextRound,
                        targetSequence = nextSeq,
                        feedbackText = "Correct! Get ready for round $nextRound."
                    )
                    demonstrateSequence(nextSeq)
                }
            } else {
                _uiState.value = _uiState.value.copy(playerInput = currentInput)
            }
        } else {
            // Wrong Step
            totalAttempts++
            _uiState.value = _uiState.value.copy(
                isGameOver = true,
                feedbackText = "Oops! Sequence missed. Tap below to try again."
            )
            voiceAssistantEngine.speakInstruction("Sequence missed. Don't worry, take a breath and try again!")
        }
    }
}
