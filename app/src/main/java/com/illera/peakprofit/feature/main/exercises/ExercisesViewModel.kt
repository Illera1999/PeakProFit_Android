package com.illera.peakprofit.feature.main.exercises

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illera.peakprofit.domain.usecase.exercise.GetExercisesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ExercisesViewModel @Inject constructor(
    private val getExercisesUseCase: GetExercisesUseCase
) : ViewModel() {
    private companion object {
        const val TAG = "ExercisesViewModel"
    }

    private val _uiState = MutableStateFlow(ExercisesUiState())
    val uiState: StateFlow<ExercisesUiState> = _uiState.asStateFlow()

    init {
        loadExercises()
    }

    fun onQueryChanged(query: String) {
        val current = _uiState.value
        val normalizedQuery = query.trim()
        val filtered = if (normalizedQuery.isBlank()) {
            current.items
        } else {
            current.items.filter { exercise ->
                exercise.name.contains(normalizedQuery, ignoreCase = true) ||
                    exercise.bodyParts.any { it.contains(normalizedQuery, ignoreCase = true) } ||
                    exercise.targetMuscles.any { it.contains(normalizedQuery, ignoreCase = true) } ||
                    exercise.equipments.any { it.contains(normalizedQuery, ignoreCase = true) }
            }
        }

        _uiState.value = current.copy(
            query = query,
            filteredItems = filtered
        )
    }

    fun retry() {
        loadExercises()
    }

    private fun loadExercises() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            runCatching { getExercisesUseCase() }
                .onSuccess { items ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        items = items,
                        filteredItems = items,
                        errorMessage = null
                    )
                }
                .onFailure {
                    Log.e(TAG, "Error cargando ejercicios", it)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "No se pudieron cargar los ejercicios. Intentalo de nuevo."
                    )
                }
        }
    }
}
