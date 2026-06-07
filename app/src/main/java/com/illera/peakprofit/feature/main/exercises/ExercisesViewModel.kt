package com.illera.peakprofit.feature.main.exercises

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illera.peakprofit.R
import com.illera.peakprofit.core.Logger
import com.illera.peakprofit.core.ui.UiText
import com.illera.peakprofit.domain.entity.AuthState
import com.illera.peakprofit.domain.entity.Exercise
import com.illera.peakprofit.domain.usecase.auth.ObserveSessionUseCase
import com.illera.peakprofit.domain.usecase.exercise.GetExerciseByIdUseCase
import com.illera.peakprofit.domain.usecase.exercise.GetExercisesUseCase
import com.illera.peakprofit.domain.usecase.exercise.ObserveSavedExercisesUseCase
import com.illera.peakprofit.domain.usecase.exercise.RemoveSavedExerciseUseCase
import com.illera.peakprofit.domain.usecase.exercise.SaveExerciseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ExercisesViewModel @Inject constructor(
    private val getExercisesUseCase: GetExercisesUseCase,
    private val getExerciseByIdUseCase: GetExerciseByIdUseCase,
    private val saveExerciseUseCase: SaveExerciseUseCase,
    private val removeSavedExerciseUseCase: RemoveSavedExerciseUseCase,
    private val observeSavedExercisesUseCase: ObserveSavedExercisesUseCase,
    private val observeSessionUseCase: ObserveSessionUseCase
) : ViewModel() {
    /**
     * Constantes de clase:
     * - TAG: etiqueta estable para trazas de logging.
     * - PAGE_SIZE: tamano de pagina acordado con el endpoint.
     */
    private companion object {
        const val TAG = "ExercisesViewModel"
        const val PAGE_SIZE = 10
    }

    private val _uiState = MutableStateFlow(ExercisesUiState())
    val uiState: StateFlow<ExercisesUiState> = _uiState.asStateFlow()
    // Marca el desplazamiento absoluto de la siguiente pagina a solicitar.
    private var currentOffset = 0
    private var currentUserId: String? = null
    private var savedExercisesJob: Job? = null

    init {
        observeAuthState()
        loadInitialExercises()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            observeSessionUseCase().collect { authState ->
                when (authState) {
                    is AuthState.Authenticated -> {
                        currentUserId = authState.session.uid
                        _uiState.value = _uiState.value.copy(canSaveExercises = true)
                        observeSavedExercises(authState.session.uid)
                    }
                    else -> {
                        currentUserId = null
                        savedExercisesJob?.cancel()
                        _uiState.value = _uiState.value.copy(
                            canSaveExercises = false,
                            savedExerciseIds = emptySet()
                        )
                    }
                }
            }
        }
    }

    fun onSaveClicked(exerciseId: String) {
        val userId = currentUserId ?: return

        viewModelScope.launch {
            runCatching {
                if (_uiState.value.savedExerciseIds.contains(exerciseId)) {
                    removeSavedExerciseUseCase(userId = userId, exerciseId = exerciseId)
                } else {
                    val exerciseDetail = getExerciseByIdUseCase(exerciseId)
                    saveExerciseUseCase(userId = userId, exercise = exerciseDetail)
                }
            }.onFailure {
                Logger.e(TAG, "Error actualizando guardado del ejercicio $exerciseId", it)
                _uiState.value = _uiState.value.copy(
                    errorMessage = UiText.StringResource(R.string.exercises_error_save)
                )
            }
        }
    }

    fun onQueryChanged(query: String) {
        val current = _uiState.value
        val filtered = filterItems(current.items, query)

        _uiState.value = current.copy(
            query = query,
            filteredItems = filtered
        )
    }

    fun retry() {
        val current = _uiState.value
        if (current.items.isEmpty()) {
            loadInitialExercises()
        } else {
            loadMore()
        }
    }

    fun loadMore() {
        val current = _uiState.value
        if (current.isLoading || current.isLoadingMore || !current.hasMore) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingMore = true, errorMessage = null)
            runCatching { getExercisesUseCase(limit = PAGE_SIZE, offset = currentOffset) }
                .onSuccess { items ->
                    val existing = _uiState.value.items
                    // Preserva el primer elemento observado ante duplicados por id.
                    val merged = (existing + items).distinctBy { it.id }
                    currentOffset += items.size
                    val hasMore = items.size == PAGE_SIZE

                    _uiState.value = _uiState.value.copy(
                        isLoadingMore = false,
                        hasMore = hasMore,
                        items = merged,
                        filteredItems = filterItems(merged, _uiState.value.query),
                        errorMessage = null
                    )
                }
                .onFailure {
                    Logger.e(TAG, "Error cargando más ejercicios", it)
                    _uiState.value = _uiState.value.copy(
                        isLoadingMore = false,
                        errorMessage = UiText.StringResource(R.string.exercises_error_load_more)
                    )
                }
        }
    }

    private fun loadInitialExercises() {
        viewModelScope.launch {
            currentOffset = 0
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                isLoadingMore = false,
                hasMore = true,
                errorMessage = null
            )
            runCatching { getExercisesUseCase(limit = PAGE_SIZE, offset = currentOffset) }
                .onSuccess { items ->
                    currentOffset = items.size
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoadingMore = false,
                        hasMore = items.size == PAGE_SIZE,
                        items = items,
                        filteredItems = filterItems(items, _uiState.value.query),
                        errorMessage = null
                    )
                }
                .onFailure {
                    Logger.e(TAG, "Error cargando ejercicios", it)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoadingMore = false,
                        errorMessage = UiText.StringResource(R.string.exercises_error_load)
                    )
                }
        }
    }

    private fun filterItems(items: List<Exercise>, query: String): List<Exercise> {
        val normalizedQuery = query.trim()
        if (normalizedQuery.isBlank()) return items

        return items.filter { exercise ->
            exercise.name.contains(normalizedQuery, ignoreCase = true) ||
                exercise.bodyParts.any { it.contains(normalizedQuery, ignoreCase = true) } ||
                exercise.targetMuscles.any { it.contains(normalizedQuery, ignoreCase = true) } ||
                exercise.equipments.any { it.contains(normalizedQuery, ignoreCase = true) }
        }
    }

    private fun observeSavedExercises(userId: String) {
        savedExercisesJob?.cancel()
        savedExercisesJob = viewModelScope.launch {
            observeSavedExercisesUseCase(userId).collect { savedExercises ->
                _uiState.value = _uiState.value.copy(
                    savedExerciseIds = savedExercises.mapTo(mutableSetOf()) { it.id }
                )
            }
        }
    }
}
