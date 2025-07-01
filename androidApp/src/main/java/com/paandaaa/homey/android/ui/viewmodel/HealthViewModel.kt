package com.paandaaa.homey.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paandaaa.homey.android.data.remote.HomeyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HealthDashboardState(
    val healthTip: String = "Ask Homey AI for a health tip!",
    val healthGoal: String = "Ask Homey AI for a personalized health goal!",
    val isLoadingTip: Boolean = false,
    val isLoadingGoal: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class HealthViewModel @Inject constructor(
    private val repository: HomeyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HealthDashboardState())
    val uiState: StateFlow<HealthDashboardState> = _uiState.asStateFlow()

    fun getDailyHealthTip(topic: String = "general wellness") {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingTip = true)
            try {
                val tip = repository.getHealthTipWithAI(topic)
                _uiState.value = _uiState.value.copy(
                    healthTip = tip,
                    isLoadingTip = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to get health tip: ${e.message}",
                    isLoadingTip = false
                )
            }
        }
    }

    fun generatePersonalizedHealthGoal(preferences: String = "healthy eating and light exercise") {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingGoal = true)
            try {
                val goal = repository.generateHealthGoalWithAI(preferences)
                _uiState.value = _uiState.value.copy(
                    healthGoal = goal,
                    isLoadingGoal = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to generate health goal: ${e.message}",
                    isLoadingGoal = false
                )
            }
        }
    }

    fun clearErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}