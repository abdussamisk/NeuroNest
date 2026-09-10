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

data class RegionalWordPair(
    val regionalWord: String,
    val languageName: String,
    val meaningEnglish: String,
    val options: List<String>,
    val correctMeaning: String
)

data class WordGameUiState(
    val currentIndex: Int = 0,
    val pairs: List<RegionalWordPair> = emptyList(),
    val score: Int = 0,
    val totalAttempts: Int = 0,
    val isCompleted: Boolean = false,
    val feedbackText: String = "Match regional words with their familiar meanings!"
)

@HiltViewModel
class WordGameViewModel @Inject constructor(
    private val cognitiveRepository: CognitiveRepository,
    private val voiceAssistantEngine: VoiceAssistantEngine
) : ViewModel() {

    private val _uiState = MutableStateFlow(WordGameUiState())
    val uiState: StateFlow<WordGameUiState> = _uiState.asStateFlow()

    private var startTimeMs: Long = System.currentTimeMillis()

    init {
        loadWordPairs()
    }

    fun loadWordPairs() {
        val wordList = listOf(
            RegionalWordPair(
                regionalWord = "চাহ (Chah)",
                languageName = "Assamese",
                meaningEnglish = "Tea",
                options = listOf("Tea ☕", "Water 💧", "Milk 🥛"),
                correctMeaning = "Tea ☕"
            ),
            RegionalWordPair(
                regionalWord = "খুরুমজরি (Khurumjari)",
                languageName = "Manipuri",
                meaningEnglish = "Greeting / Welcome",
                options = listOf("Greeting / Welcome 🙏", "Thank You", "Good Night"),
                correctMeaning = "Greeting / Welcome 🙏"
            ),
            RegionalWordPair(
                regionalWord = "ভাল (Bhal)",
                languageName = "Bengali / Assamese",
                meaningEnglish = "Good / Well",
                options = listOf("Good / Well 👍", "Bad", "Cold"),
                correctMeaning = "Good / Well 👍"
            )
        )

        _uiState.value = WordGameUiState(
            currentIndex = 0,
            pairs = wordList,
            score = 0,
            totalAttempts = 0,
            isCompleted = false
        )
        startTimeMs = System.currentTimeMillis()
        voiceAssistantEngine.speakInstruction("Regional Word Matching game! Select the correct meaning for each word.")
    }

    fun onOptionSelected(option: String) {
        val state = _uiState.value
        if (state.isCompleted) return

        val currentPair = state.pairs[state.currentIndex]
        val attempts = state.totalAttempts + 1

        if (option == currentPair.correctMeaning) {
            val newScore = state.score + 30
            val nextIndex = state.currentIndex + 1

            if (nextIndex >= state.pairs.size) {
                _uiState.value = state.copy(
                    score = newScore,
                    totalAttempts = attempts,
                    isCompleted = true,
                    feedbackText = "Outstanding! Word matching exercise completed!"
                )
                val reactionTime = System.currentTimeMillis() - startTimeMs
                viewModelScope.launch {
                    cognitiveRepository.recordGameSession(
                        gameType = "WORD_MATCHING",
                        score = newScore,
                        totalAttempts = attempts,
                        successfulAttempts = state.pairs.size,
                        avgReactionTimeMs = reactionTime / state.pairs.size
                    )
                }
                voiceAssistantEngine.speakInstruction("Wonderful! You matched all regional word pairs correctly!")
            } else {
                _uiState.value = state.copy(
                    currentIndex = nextIndex,
                    score = newScore,
                    totalAttempts = attempts,
                    feedbackText = "Correct word pair! Moving to next."
                )
            }
        } else {
            _uiState.value = state.copy(
                totalAttempts = attempts,
                feedbackText = "Not quite. Try another option!"
            )
        }
    }
}
