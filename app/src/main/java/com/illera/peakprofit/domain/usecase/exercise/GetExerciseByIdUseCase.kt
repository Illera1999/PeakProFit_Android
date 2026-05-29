package com.illera.peakprofit.domain.usecase.exercise

import com.illera.peakprofit.domain.entity.Exercise
import com.illera.peakprofit.domain.repository.ExerciseRepository
import javax.inject.Inject

class GetExerciseByIdUseCase @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) {
    suspend operator fun invoke(id: String): Exercise = exerciseRepository.getExerciseById(id)
}
