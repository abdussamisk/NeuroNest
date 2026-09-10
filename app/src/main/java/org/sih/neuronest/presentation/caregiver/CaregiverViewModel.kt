package org.sih.neuronest.presentation.caregiver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sih.neuronest.data.local.entity.CaregiverAlert
import org.sih.neuronest.data.local.entity.CognitiveSession
import org.sih.neuronest.data.local.entity.PatientProfile
import org.sih.neuronest.data.repository.CognitiveRepository
import javax.inject.Inject

data class CaregiverUiState(
    val profile: PatientProfile = PatientProfile(),
    val sessions: List<CognitiveSession> = emptyList(),
    val alerts: List<CaregiverAlert> = emptyList(),
    val averageAccuracy: Float = 86.4f,
    val averageReactionTimeMs: Long = 3420,
    val isSyncing: Boolean = false
)

@HiltViewModel
class CaregiverViewModel @Inject constructor(
    private val cognitiveRepository: CognitiveRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CaregiverUiState())
    val uiState: StateFlow<CaregiverUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            cognitiveRepository.patientProfileFlow.collect { profile ->
                profile?.let { p ->
                    _uiState.value = _uiState.value.copy(profile = p)
                }
            }
        }

        viewModelScope.launch {
            cognitiveRepository.allSessionsFlow.collect { sessions ->
                val avgAcc = if (sessions.isNotEmpty()) sessions.map { it.accuracyPercentage }.average().toFloat() else 86.4f
                val avgReact = if (sessions.isNotEmpty()) sessions.map { it.averageReactionTimeMs }.average().toLong() else 3420L

                _uiState.value = _uiState.value.copy(
                    sessions = sessions,
                    averageAccuracy = avgAcc,
                    averageReactionTimeMs = avgReact
                )
            }
        }

        viewModelScope.launch {
            cognitiveRepository.allAlertsFlow.collect { alerts ->
                _uiState.value = _uiState.value.copy(alerts = alerts)
            }
        }
    }

    fun dismissAlert(alertId: String) {
        viewModelScope.launch {
            cognitiveRepository.dismissAlert(alertId)
        }
    }
}
