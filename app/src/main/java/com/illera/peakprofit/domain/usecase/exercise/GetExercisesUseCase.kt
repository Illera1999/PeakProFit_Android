package com.illera.peakprofit.domain.usecase.exercise

import com.illera.peakprofit.domain.entity.Exercise
import com.illera.peakprofit.domain.repository.ExerciseRepository
import javax.inject.Inject

class GetExercisesUseCase @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) {
    suspend operator fun invoke(limit: Int, offset: Int): List<Exercise> =
        exerciseRepository.getExercises(limit = limit, offset = offset)
}
