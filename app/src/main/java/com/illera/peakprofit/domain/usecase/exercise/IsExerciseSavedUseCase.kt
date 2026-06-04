package com.illera.peakprofit.domain.usecase.exercise

import com.illera.peakprofit.domain.repository.SavedExerciseRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class IsExerciseSavedUseCase @Inject constructor(
    private val savedExerciseRepository: SavedExerciseRepository
) {
    operator fun invoke(userId: String, exerciseId: String): Flow<Boolean> {
        return savedExerciseRepository.isExerciseSaved(userId = userId, exerciseId = exerciseId)
    }
}
