package com.illera.peakprofit.feature.exercises

import com.illera.peakprofit.domain.entity.Exercise

data class ExercisesUiState(
    val isLoading: Boolean = false,
    val query: String = "",
    val items: List<Exercise> = emptyList(),
    val filteredItems: List<Exercise> = emptyList(),
    val errorMessage: String? = null
)
