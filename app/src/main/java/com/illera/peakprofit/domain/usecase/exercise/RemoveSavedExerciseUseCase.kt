package com.illera.peakprofit.domain.usecase.exercise

import com.illera.peakprofit.domain.repository.SavedExerciseRepository
import javax.inject.Inject

class RemoveSavedExerciseUseCase @Inject constructor(
    private val savedExerciseRepository: SavedExerciseRepository
) {
    suspend operator fun invoke(userId: String, exerciseId: String) {
        savedExerciseRepository.removeSavedExercise(userId = userId, exerciseId = exerciseId)
    }
}
