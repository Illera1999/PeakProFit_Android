package com.illera.peakprofit.feature.main.saved_exercises

import com.illera.peakprofit.domain.entity.Exercise

data class SavedExercisesUiState(
    val isAuthenticated: Boolean = false,
    val items: List<Exercise> = emptyList(),
    val errorMessage: String? = null
)
