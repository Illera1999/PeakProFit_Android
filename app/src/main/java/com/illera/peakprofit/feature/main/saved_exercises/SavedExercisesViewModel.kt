package com.illera.peakprofit.feature.main.saved_exercises

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illera.peakprofit.domain.entity.AuthState
import com.illera.peakprofit.domain.usecase.auth.ObserveSessionUseCase
import com.illera.peakprofit.domain.usecase.exercise.ObserveSavedExercisesUseCase
import com.illera.peakprofit.domain.usecase.exercise.RemoveSavedExerciseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SavedExercisesViewModel @Inject constructor(
    private val observeSessionUseCase: ObserveSessionUseCase,
    private val observeSavedExercisesUseCase: ObserveSavedExercisesUseCase,
    private val removeSavedExerciseUseCase: RemoveSavedExerciseUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SavedExercisesUiState())
    val uiState: StateFlow<SavedExercisesUiState> = _uiState.asStateFlow()
    private var currentUserId: String? = null
    private var savedExercisesJob: Job? = null

    init {
        observeSession()
    }

    fun onSaveClicked(exerciseId: String) {
        val userId = currentUserId ?: return
        viewModelScope.launch {
            runCatching {
                removeSavedExerciseUseCase(userId = userId, exerciseId = exerciseId)
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "No se pudo actualizar el ejercicio guardado. Intentalo de nuevo."
                )
            }
        }
    }

    private fun observeSession() {
        viewModelScope.launch {
            observeSessionUseCase().collect { authState ->
                when (authState) {
                    is AuthState.Authenticated -> {
                        currentUserId = authState.session.uid
                        _uiState.value = _uiState.value.copy(isAuthenticated = true, errorMessage = null)
                        observeSavedExercises(authState.session.uid)
                    }
                    else -> {
                        currentUserId = null
                        savedExercisesJob?.cancel()
                        _uiState.value = SavedExercisesUiState(isAuthenticated = false)
                    }
                }
            }
        }
    }

    private fun observeSavedExercises(userId: String) {
        savedExercisesJob?.cancel()
        savedExercisesJob = viewModelScope.launch {
            observeSavedExercisesUseCase(userId).collect { items ->
                _uiState.value = _uiState.value.copy(
                    isAuthenticated = true,
                    items = items,
                    errorMessage = null
                )
            }
        }
    }
}
