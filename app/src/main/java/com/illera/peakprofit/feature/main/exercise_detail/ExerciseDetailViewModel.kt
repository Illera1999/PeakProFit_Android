package com.illera.peakprofit.feature.main.exercise_detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illera.peakprofit.domain.entity.AuthState
import com.illera.peakprofit.domain.usecase.auth.ObserveSessionUseCase
import com.illera.peakprofit.domain.usecase.exercise.GetExerciseByIdUseCase
import com.illera.peakprofit.domain.usecase.exercise.GetExerciseImageByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

@HiltViewModel
class ExerciseDetailViewModel @Inject constructor(
    private val getExerciseByIdUseCase: GetExerciseByIdUseCase,
    private val getExerciseImageByIdUseCase: GetExerciseImageByIdUseCase,
    private val observeSessionUseCase: ObserveSessionUseCase
) : ViewModel() {
    private companion object {
        const val TAG = "ExerciseDetailViewModel"
    }

    private val _uiState = MutableStateFlow(ExerciseDetailUiState())
    val uiState: StateFlow<ExerciseDetailUiState> = _uiState.asStateFlow()

    init {
        observeAuthState()
    }

    fun load(exerciseId: String) {
        val current = _uiState.value
        if (current.exercise?.id == exerciseId && current.errorMessage == null) return

        viewModelScope.launch {
            _uiState.value = current.copy(
                isLoading = true,
                exercise = null,
                imageData = null,
                errorMessage = null
            )
            supervisorScope {
                val exerciseDeferred = async { getExerciseByIdUseCase(exerciseId) }
                val imageDeferred = async { getExerciseImageByIdUseCase(exerciseId) }

                runCatching { exerciseDeferred.await() }
                    .onSuccess { exercise ->
                        val imageData = runCatching { imageDeferred.await() }
                            .onFailure {
                                Log.w(TAG, "No se pudo cargar la imagen del ejercicio $exerciseId", it)
                            }
                            .getOrNull()

                        _uiState.value = ExerciseDetailUiState(
                            isLoading = false,
                            exercise = exercise,
                            imageData = imageData,
                            canSaveExercise = _uiState.value.canSaveExercise,
                            errorMessage = null
                        )
                    }
                    .onFailure {
                        Log.e(TAG, "Error cargando detalle del ejercicio $exerciseId", it)
                        _uiState.value = ExerciseDetailUiState(
                            isLoading = false,
                            exercise = null,
                            canSaveExercise = _uiState.value.canSaveExercise,
                            errorMessage = "No se pudo cargar el detalle del ejercicio."
                        )
                    }
            }
        }
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            observeSessionUseCase().collect { authState ->
                _uiState.value = _uiState.value.copy(
                    canSaveExercise = authState is AuthState.Authenticated
                )
            }
        }
    }
}
