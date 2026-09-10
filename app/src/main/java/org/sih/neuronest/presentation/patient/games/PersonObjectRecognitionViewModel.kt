package org.sih.neuronest.presentation.patient.games

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sih.neuronest.data.repository.CognitiveRepository
import org.sih.neuronest.domain.ai.MLKitRecognizer
import org.sih.neuronest.domain.ai.VoiceAssistantEngine
import javax.inject.Inject

data class MemoryPersonCard(
    val title: String,
    val relationTag: String,
    val clueText: String,
    val iconEmoji: String,
    val options: List<String>,
    val correctName: String
)

data class PersonObjectUiState(
    val currentIndex: Int = 0,
    val cards: List<MemoryPersonCard> = emptyList(),
    val score: Int = 0,
    val totalAttempts: Int = 0,
    val isCompleted: Boolean = false,
    val feedbackText: String = "Identify family members & familiar objects!"
)

@HiltViewModel
class PersonObjectRecognitionViewModel @Inject constructor(
    private val cognitiveRepository: CognitiveRepository,
    private val mlKitRecognizer: MLKitRecognizer,
    private val voiceAssistantEngine: VoiceAssistantEngine
) : ViewModel() {

    private val _uiState = MutableStateFlow(PersonObjectUiState())
    val uiState: StateFlow<PersonObjectUiState> = _uiState.asStateFlow()

    private var startTimeMs: Long = System.currentTimeMillis()

    init {
        loadCards()
    }

    fun loadCards() {
        val sampleCards = listOf(
            MemoryPersonCard(
                title = "Who is this family member?",
                relationTag = "GRANDSON",
                clueText = "Loves playing cricket and visits you every Sunday from Guwahati.",
                iconEmoji = "👦",
                options = listOf("Rahul (Grandson)", "Amit (Nephew)", "Suresh (Neighbor)"),
                correctName = "Rahul (Grandson)"
            ),
            MemoryPersonCard(
                title = "Identify this traditional object",
                relationTag = "CRAFT",
                clueText = "Traditional hand-woven bamboo hat used in tea gardens and fields.",
                iconEmoji = "👒",
                options = listOf("Jaapi (Bamboo Hat)", "Clay Pot", "Wooden Stool"),
                correctName = "Jaapi (Bamboo Hat)"
            ),
            MemoryPersonCard(
                title = "Identify this family place",
                relationTag = "HOME",
                clueText = "Ancestral home near the Brahmaputra river in Tezpur.",
                iconEmoji = "🏡",
                options = listOf("Tezpur Home", "Guwahati Flat", "Shillong Cottage"),
                correctName = "Tezpur Home"
            )
        )

        _uiState.value = PersonObjectUiState(
            currentIndex = 0,
            cards = sampleCards,
            score = 0,
            totalAttempts = 0,
            isCompleted = false
        )
        startTimeMs = System.currentTimeMillis()
        voiceAssistantEngine.speakInstruction("Person and Object Memory game. Identify the familiar faces and items!")
    }

    fun onOptionSelected(selectedName: String) {
        val state = _uiState.value
        if (state.isCompleted) return

        val currentCard = state.cards[state.currentIndex]
        val attempts = state.totalAttempts + 1

        val recognition = mlKitRecognizer.analyzeMemoryCueImage(currentCard.iconEmoji, selectedName)

        if (selectedName == currentCard.correctName || recognition.isRecognizedMatch) {
            val newScore = state.score + 35
            val nextIdx = state.currentIndex + 1

            if (nextIdx >= state.cards.size) {
                _uiState.value = state.copy(
                    score = newScore,
                    totalAttempts = attempts,
                    isCompleted = true,
                    feedbackText = "Excellent! You recognized all family cues and objects!"
                )
                val reactionTime = System.currentTimeMillis() - startTimeMs
                viewModelScope.launch {
                    cognitiveRepository.recordGameSession(
                        gameType = "PERSON_OBJECT_RECOGNITION",
                        score = newScore,
                        totalAttempts = attempts,
                        successfulAttempts = state.cards.size,
                        avgReactionTimeMs = reactionTime / state.cards.size
                    )
                }
                voiceAssistantEngine.speakInstruction("Wonderful memory! You recognized all loved ones and places.")
            } else {
                _uiState.value = state.copy(
                    currentIndex = nextIdx,
                    score = newScore,
                    totalAttempts = attempts,
                    feedbackText = "Correct! Recognized ${currentCard.correctName}."
                )
            }
        } else {
            _uiState.value = state.copy(
                totalAttempts = attempts,
                feedbackText = "Take your time. Read the clue again!"
            )
        }
    }
}
