package com.illera.peakprofit.feature.main.exercise_detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illera.peakprofit.domain.usecase.exercise.GetExerciseByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ExerciseDetailViewModel @Inject constructor(
    private val getExerciseByIdUseCase: GetExerciseByIdUseCase
) : ViewModel() {
    private companion object {
        const val TAG = "ExerciseDetailViewModel"
    }

    private val _uiState = MutableStateFlow(ExerciseDetailUiState())
    val uiState: StateFlow<ExerciseDetailUiState> = _uiState.asStateFlow()

    fun load(exerciseId: String) {
        val current = _uiState.value
        if (current.exercise?.id == exerciseId && current.errorMessage == null) return

        viewModelScope.launch {
            _uiState.value = ExerciseDetailUiState(isLoading = true)
            runCatching { getExerciseByIdUseCase(exerciseId) }
                .onSuccess { exercise ->
                    _uiState.value = ExerciseDetailUiState(
                        isLoading = false,
                        exercise = exercise,
                        errorMessage = null
                    )
                }
                .onFailure {
                    Log.e(TAG, "Error cargando detalle del ejercicio $exerciseId", it)
                    _uiState.value = ExerciseDetailUiState(
                        isLoading = false,
                        exercise = null,
                        errorMessage = "No se pudo cargar el detalle del ejercicio."
                    )
                }
        }
    }
}
