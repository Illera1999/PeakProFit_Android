package com.illera.peakprofit.domain.usecase.exercise

import com.illera.peakprofit.domain.entity.Exercise
import com.illera.peakprofit.domain.repository.SavedExerciseRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveSavedExercisesUseCase @Inject constructor(
    private val savedExerciseRepository: SavedExerciseRepository
) {
    operator fun invoke(userId: String): Flow<List<Exercise>> {
        return savedExerciseRepository.observeSavedExercises(userId)
    }
}
