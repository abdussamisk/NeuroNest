package org.sih.neuronest.presentation.patient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sih.neuronest.data.local.entity.PatientProfile
import org.sih.neuronest.data.local.entity.RoutineReminder
import org.sih.neuronest.data.repository.CognitiveRepository
import org.sih.neuronest.data.repository.RoutineRepository
import org.sih.neuronest.domain.ai.VoiceAssistantEngine
import org.sih.neuronest.theme.NERRegion
import javax.inject.Inject

data class PatientHomeUiState(
    val profile: PatientProfile = PatientProfile(),
    val reminders: List<RoutineReminder> = emptyList(),
    val currentRegion: NERRegion = NERRegion.ASSAM,
    val isVoiceSpeaking: Boolean = false,
    val isLoading: Boolean = false
)

@HiltViewModel
class PatientViewModel @Inject constructor(
    private val cognitiveRepository: CognitiveRepository,
    private val routineRepository: RoutineRepository,
    private val voiceAssistantEngine: VoiceAssistantEngine
) : ViewModel() {

    private val _uiState = MutableStateFlow(PatientHomeUiState())
    val uiState: StateFlow<PatientHomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // Seed routine DB if empty
            routineRepository.seedInitialDataIfEmpty()

            // Observe patient profile
            cognitiveRepository.patientProfileFlow.collect { profile ->
                profile?.let { p ->
                    val regionEnum = try {
                        NERRegion.valueOf(p.selectedRegion)
                    } catch (e: Exception) {
                        NERRegion.ASSAM
                    }
                    _uiState.value = _uiState.value.copy(
                        profile = p,
                        currentRegion = regionEnum
                    )
                }
            }
        }

        viewModelScope.launch {
            routineRepository.remindersFlow.collect { reminders ->
                _uiState.value = _uiState.value.copy(reminders = reminders)
            }
        }

        viewModelScope.launch {
            voiceAssistantEngine.isSpeaking.collect { isSpeaking ->
                _uiState.value = _uiState.value.copy(isVoiceSpeaking = isSpeaking)
            }
        }
    }

    fun speakHomeGreeting() {
        val region = _uiState.value.currentRegion
        val text = "${region.backgroundGreeting} Welcome to NeuroNest. Select a cognitive exercise below, or check your daily reminders."
        voiceAssistantEngine.speakInstruction(text, _uiState.value.profile.preferredLanguage)
    }

    fun selectNextRegion() {
        val regions = NERRegion.values()
        val nextIndex = (regions.indexOf(_uiState.value.currentRegion) + 1) % regions.size
        val newRegion = regions[nextIndex]
        viewModelScope.launch {
            cognitiveRepository.updateRegion(newRegion.name)
        }
    }

    fun acknowledgeReminder(id: String) {
        viewModelScope.launch {
            routineRepository.acknowledgeReminder(id)
        }
    }

    override fun onCleared() {
        super.onCleared()
        voiceAssistantEngine.stopSpeaking()
    }
}
