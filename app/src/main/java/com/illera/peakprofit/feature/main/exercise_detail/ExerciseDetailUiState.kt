package com.illera.peakprofit.feature.main.exercise_detail

import com.illera.peakprofit.core.ui.UiText
import com.illera.peakprofit.domain.entity.Exercise
import java.io.File

data class ExerciseDetailUiState(
    val isLoading: Boolean = true,
    val exercise: Exercise? = null,
    val imageFile: File? = null,
    val canSaveExercise: Boolean = false,
    val isExerciseSaved: Boolean = false,
    val errorMessage: UiText? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ExerciseDetailUiState

        if (isLoading != other.isLoading) return false
        if (canSaveExercise != other.canSaveExercise) return false
        if (isExerciseSaved != other.isExerciseSaved) return false
        if (exercise != other.exercise) return false
        if (imageFile?.path != other.imageFile?.path) return false
        if (errorMessage != other.errorMessage) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isLoading.hashCode()
        result = 31 * result + canSaveExercise.hashCode()
        result = 31 * result + isExerciseSaved.hashCode()
        result = 31 * result + (exercise?.hashCode() ?: 0)
        result = 31 * result + (imageFile?.path?.hashCode() ?: 0)
        result = 31 * result + (errorMessage?.hashCode() ?: 0)
        return result
    }
}
