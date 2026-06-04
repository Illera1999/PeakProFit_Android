package com.illera.peakprofit.feature.main.exercise_detail

import com.illera.peakprofit.domain.entity.Exercise

data class ExerciseDetailUiState(
    val isLoading: Boolean = true,
    val exercise: Exercise? = null,
    val imageData: ByteArray? = null,
    val canSaveExercise: Boolean = false,
    val isExerciseSaved: Boolean = false,
    val errorMessage: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ExerciseDetailUiState

        if (isLoading != other.isLoading) return false
        if (canSaveExercise != other.canSaveExercise) return false
        if (isExerciseSaved != other.isExerciseSaved) return false
        if (exercise != other.exercise) return false
        if (!imageData.contentEquals(other.imageData)) return false
        if (errorMessage != other.errorMessage) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isLoading.hashCode()
        result = 31 * result + canSaveExercise.hashCode()
        result = 31 * result + isExerciseSaved.hashCode()
        result = 31 * result + (exercise?.hashCode() ?: 0)
        result = 31 * result + (imageData?.contentHashCode() ?: 0)
        result = 31 * result + (errorMessage?.hashCode() ?: 0)
        return result
    }
}
