package com.illera.peakprofit.feature.main.exercise_detail

import com.illera.peakprofit.domain.entity.Exercise

data class ExerciseDetailUiState(
    val isLoading: Boolean = true,
    val exercise: Exercise? = null,
    val errorMessage: String? = null
)
