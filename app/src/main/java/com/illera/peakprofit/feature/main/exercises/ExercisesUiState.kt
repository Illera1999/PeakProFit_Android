package com.illera.peakprofit.feature.main.exercises

import com.illera.peakprofit.core.ui.UiText
import com.illera.peakprofit.domain.entity.Exercise

data class ExercisesUiState(
    val canSaveExercises: Boolean = false,
    val savedExerciseIds: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isSearching: Boolean = false,
    val hasMore: Boolean = true,
    val query: String = "",
    val items: List<Exercise> = emptyList(),
    val searchResults: List<Exercise> = emptyList(),
    val errorMessage: UiText? = null
) {
    val isSearchMode: Boolean
        get() = query.isNotBlank()

    val visibleItems: List<Exercise>
        get() = if (isSearchMode) searchResults else items
}
