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

data class PatternQuestion(
    val title: String,
    val patternSequence: List<String>, // e.g. ["🔴", "🟢", "🔴", "❓"]
    val options: List<String>,
    val correctAnswer: String
)

data class PatternRecognitionUiState(
    val currentQuestionIndex: Int = 0,
    val questions: List<PatternQuestion> = emptyList(),
    val score: Int = 0,
    val totalAttempts: Int = 0,
    val isCompleted: Boolean = false,
    val feedbackText: String = "Complete the traditional textile pattern sequence!"
)

@HiltViewModel
class PatternRecognitionViewModel @Inject constructor(
    private val cognitiveRepository: CognitiveRepository,
    private val voiceAssistantEngine: VoiceAssistantEngine
) : ViewModel() {

    private val _uiState = MutableStateFlow(PatternRecognitionUiState())
    val uiState: StateFlow<PatternRecognitionUiState> = _uiState.asStateFlow()

    private var startTimeMs: Long = System.currentTimeMillis()

    init {
        loadQuestions()
    }

    fun loadQuestions() {
        val qList = listOf(
            PatternQuestion(
                title = "Assamese Mekhela Motif Sequence",
                patternSequence = listOf("🌺", "🍃", "🌺", "❓"),
                options = listOf("🌺", "🍃", "🌾", "🌸"),
                correctAnswer = "🍃"
            ),
            PatternQuestion(
                title = "Naga Textile Diamond Pattern",
                patternSequence = listOf("🔶", "🔷", "🔶", "🔷", "❓"),
                options = listOf("🔴", "🔶", "🔷", "⭐"),
                correctAnswer = "🔶"
            ),
            PatternQuestion(
                title = "Manipuri Phanek Traditional Motif",
                patternSequence = listOf("🟨", "⬛", "🟨", "⬛", "❓"),
                options = listOf("🟨", "🟦", "⬛", "🟥"),
                correctAnswer = "🟨"
            )
        )

        _uiState.value = PatternRecognitionUiState(
            currentQuestionIndex = 0,
            questions = qList,
            score = 0,
            totalAttempts = 0,
            isCompleted = false
        )
        startTimeMs = System.currentTimeMillis()
        voiceAssistantEngine.speakInstruction("Complete the traditional weaving pattern sequence!")
    }

    fun onOptionSelected(selectedOption: String) {
        val state = _uiState.value
        if (state.isCompleted) return

        val currentQ = state.questions[state.currentQuestionIndex]
        val attempts = state.totalAttempts + 1

        if (selectedOption == currentQ.correctAnswer) {
            val newScore = state.score + 30
            val nextIndex = state.currentQuestionIndex + 1

            if (nextIndex >= state.questions.size) {
                // All questions completed!
                _uiState.value = state.copy(
                    score = newScore,
                    totalAttempts = attempts,
                    isCompleted = true,
                    feedbackText = "Wonderful! You completed all textile weaving patterns!"
                )
                val reactionTime = System.currentTimeMillis() - startTimeMs
                viewModelScope.launch {
                    cognitiveRepository.recordGameSession(
                        gameType = "PATTERN_RECOGNITION",
                        score = newScore,
                        totalAttempts = attempts,
                        successfulAttempts = state.questions.size,
                        avgReactionTimeMs = reactionTime / state.questions.size
                    )
                }
                voiceAssistantEngine.speakInstruction("Pattern exercise finished! Excellent work!")
            } else {
                _uiState.value = state.copy(
                    currentQuestionIndex = nextIndex,
                    score = newScore,
                    totalAttempts = attempts,
                    feedbackText = "Correct pattern! Moving to next motif."
                )
            }
        } else {
            _uiState.value = state.copy(
                totalAttempts = attempts,
                feedbackText = "Not quite right. Look at the sequence again!"
            )
        }
    }
}
