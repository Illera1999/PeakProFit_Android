package com.illera.peakprofit.domain.usecase.exercise

import com.illera.peakprofit.domain.entity.Exercise
import com.illera.peakprofit.domain.repository.SavedExerciseRepository
import javax.inject.Inject

class GetSavedExerciseByIdUseCase @Inject constructor(
    private val savedExerciseRepository: SavedExerciseRepository
) {
    suspend operator fun invoke(userId: String, exerciseId: String): Exercise? {
        return savedExerciseRepository.getSavedExerciseById(userId = userId, exerciseId = exerciseId)
    }
}
