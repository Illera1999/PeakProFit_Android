package com.illera.peakprofit.feature.main.exercise_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illera.peakprofit.R
import com.illera.peakprofit.core.Logger
import com.illera.peakprofit.core.ui.UiText
import com.illera.peakprofit.domain.entity.AuthState
import com.illera.peakprofit.domain.usecase.auth.ObserveSessionUseCase
import com.illera.peakprofit.domain.usecase.exercise.GetExerciseByIdUseCase
import com.illera.peakprofit.domain.usecase.exercise.GetExerciseImageByIdUseCase
import com.illera.peakprofit.domain.usecase.exercise.IsExerciseSavedUseCase
import com.illera.peakprofit.domain.usecase.exercise.RemoveSavedExerciseUseCase
import com.illera.peakprofit.domain.usecase.exercise.SaveExerciseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

@HiltViewModel
class ExerciseDetailViewModel @Inject constructor(
    private val getExerciseByIdUseCase: GetExerciseByIdUseCase,
    private val getExerciseImageByIdUseCase: GetExerciseImageByIdUseCase,
    private val saveExerciseUseCase: SaveExerciseUseCase,
    private val removeSavedExerciseUseCase: RemoveSavedExerciseUseCase,
    private val isExerciseSavedUseCase: IsExerciseSavedUseCase,
    private val observeSessionUseCase: ObserveSessionUseCase
) : ViewModel() {
    private companion object {
        const val TAG = "ExerciseDetailViewModel"
    }

    private val _uiState = MutableStateFlow(ExerciseDetailUiState())
    val uiState: StateFlow<ExerciseDetailUiState> = _uiState.asStateFlow()
    private var currentUserId: String? = null
    private var currentExerciseId: String? = null
    private var savedStateJob: Job? = null

    init {
        observeAuthState()
    }

    fun load(exerciseId: String) {
        currentExerciseId = exerciseId
        observeSavedState()
        val current = _uiState.value
        if (current.exercise?.id == exerciseId && current.errorMessage == null) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                exercise = null,
                imageFile = null,
                errorMessage = null
            )
            supervisorScope {
                val exerciseDeferred = async { getExerciseByIdUseCase(exerciseId) }
                val imageDeferred = async { getExerciseImageByIdUseCase(exerciseId) }

                runCatching { exerciseDeferred.await() }
                    .onSuccess { exercise ->
                        val imageFile = runCatching { imageDeferred.await() }
                            .onFailure {
                                Logger.w(
                                    TAG,
                                    "No se pudo cargar la imagen del ejercicio $exerciseId",
                                    it
                                )
                            }
                            .getOrNull()

                        _uiState.value = ExerciseDetailUiState(
                            isLoading = false,
                            exercise = exercise,
                            imageFile = imageFile,
                            canSaveExercise = _uiState.value.canSaveExercise,
                            isExerciseSaved = _uiState.value.isExerciseSaved,
                            errorMessage = null
                        )
                    }
                    .onFailure {
                        Logger.e(TAG, "Error cargando detalle del ejercicio $exerciseId", it)
                        _uiState.value = ExerciseDetailUiState(
                            isLoading = false,
                            exercise = null,
                            canSaveExercise = _uiState.value.canSaveExercise,
                            isExerciseSaved = _uiState.value.isExerciseSaved,
                            errorMessage = UiText.StringResource(R.string.exercise_detail_error_load)
                        )
                    }
            }
        }
    }

    fun onSaveClicked() {
        val userId = currentUserId ?: return
        val exercise = _uiState.value.exercise ?: return

        viewModelScope.launch {
            runCatching {
                if (_uiState.value.isExerciseSaved) {
                    removeSavedExerciseUseCase(userId = userId, exerciseId = exercise.id)
                } else {
                    saveExerciseUseCase(userId = userId, exercise = exercise)
                }
            }.onFailure {
                Logger.e(TAG, "Error actualizando guardado del ejercicio ${exercise.id}", it)
                _uiState.value = _uiState.value.copy(
                    errorMessage = UiText.StringResource(R.string.exercise_detail_error_save)
                )
            }
        }
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            observeSessionUseCase().collect { authState ->
                when (authState) {
                    is AuthState.Authenticated -> {
                        currentUserId = authState.session.uid
                        _uiState.value = _uiState.value.copy(canSaveExercise = true)
                        observeSavedState()
                    }
                    else -> {
                        currentUserId = null
                        savedStateJob?.cancel()
                        _uiState.value = _uiState.value.copy(
                            canSaveExercise = false,
                            isExerciseSaved = false
                        )
                    }
                }
            }
        }
    }

    private fun observeSavedState() {
        val userId = currentUserId ?: return
        val exerciseId = currentExerciseId ?: return

        savedStateJob?.cancel()
        savedStateJob = viewModelScope.launch {
            isExerciseSavedUseCase(userId = userId, exerciseId = exerciseId).collect { isSaved ->
                _uiState.value = _uiState.value.copy(isExerciseSaved = isSaved)
            }
        }
    }
}
