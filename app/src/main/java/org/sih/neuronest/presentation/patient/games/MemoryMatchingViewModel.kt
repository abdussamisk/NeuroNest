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

data class MemoryCardItem(
    val id: Int,
    val emoji: String,
    val title: String,
    var isFaceUp: Boolean = false,
    var isMatched: Boolean = false
)

data class MemoryMatchingUiState(
    val cards: List<MemoryCardItem> = emptyList(),
    val score: Int = 0,
    val totalAttempts: Int = 0,
    val matchesFound: Int = 0,
    val isCompleted: Boolean = false,
    val feedbackMessage: String = "Tap cards to find matching North Eastern icons!"
)

@HiltViewModel
class MemoryMatchingViewModel @Inject constructor(
    private val cognitiveRepository: CognitiveRepository,
    private val voiceAssistantEngine: VoiceAssistantEngine
) : ViewModel() {

    private val _uiState = MutableStateFlow(MemoryMatchingUiState())
    val uiState: StateFlow<MemoryMatchingUiState> = _uiState.asStateFlow()

    private var firstSelectedIndex: Int? = null
    private var startTimeMs: Long = System.currentTimeMillis()

    init {
        resetGame()
    }

    fun resetGame() {
        val sampleItems = listOf(
            "🌸" to "Rhino Flower",
            "🦏" to "Kaziranga Rhino",
            "🦚" to "Peacock",
            "🍵" to "Assam Tea Leaf"
        )
        val cardList = (sampleItems + sampleItems).shuffled().mapIndexed { index, pair ->
            MemoryCardItem(id = index, emoji = pair.first, title = pair.second)
        }
        _uiState.value = MemoryMatchingUiState(
            cards = cardList,
            score = 0,
            totalAttempts = 0,
            matchesFound = 0,
            isCompleted = false
        )
        startTimeMs = System.currentTimeMillis()
        voiceAssistantEngine.speakInstruction("Memory Matching game started. Tap matching North Eastern cards!")
    }

    fun onCardClick(index: Int) {
        val currentCards = _uiState.value.cards.toMutableList()
        val card = currentCards[index]

        if (card.isFaceUp || card.isMatched || _uiState.value.isCompleted) return

        card.isFaceUp = true
        _uiState.value = _uiState.value.copy(cards = currentCards)

        if (firstSelectedIndex == null) {
            firstSelectedIndex = index
        } else {
            val firstIdx = firstSelectedIndex!!
            val firstCard = currentCards[firstIdx]

            val attempts = _uiState.value.totalAttempts + 1

            if (firstCard.emoji == card.emoji) {
                // Match Found!
                firstCard.isMatched = true
                card.isMatched = true
                val matches = _uiState.value.matchesFound + 1
                val newScore = _uiState.value.score + 20

                firstSelectedIndex = null
                val completed = matches >= 4

                _uiState.value = _uiState.value.copy(
                    cards = currentCards,
                    score = newScore,
                    totalAttempts = attempts,
                    matchesFound = matches,
                    isCompleted = completed,
                    feedbackMessage = if (completed) "Wonderful! Exercise Completed!" else "Match Found! Great memory!"
                )

                if (completed) {
                    val reactionTime = System.currentTimeMillis() - startTimeMs
                    viewModelScope.launch {
                        cognitiveRepository.recordGameSession(
                            gameType = "MEMORY_MATCH",
                            score = newScore,
                            totalAttempts = attempts,
                            successfulAttempts = 4,
                            avgReactionTimeMs = reactionTime / 4
                        )
                    }
                    voiceAssistantEngine.speakInstruction("Congratulations! You matched all regional cards successfully!")
                }
            } else {
                // Mismatch
                _uiState.value = _uiState.value.copy(
                    totalAttempts = attempts,
                    feedbackMessage = "Not a match. Keep trying!"
                )
                viewModelScope.launch {
                    kotlinx.coroutines.delay(1000)
                    firstCard.isFaceUp = false
                    card.isFaceUp = false
                    firstSelectedIndex = null
                    _uiState.value = _uiState.value.copy(cards = _uiState.value.cards.toMutableList())
                }
            }
        }
    }
}
