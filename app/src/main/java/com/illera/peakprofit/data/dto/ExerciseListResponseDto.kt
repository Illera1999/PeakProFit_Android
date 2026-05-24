package com.illera.peakprofit.data.dto

data class ExerciseListResponseDto(
    val success: Boolean? = null,
    val data: List<ExerciseDto> = emptyList()
)
