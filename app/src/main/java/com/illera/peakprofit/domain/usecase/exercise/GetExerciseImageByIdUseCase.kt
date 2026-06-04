package com.illera.peakprofit.domain.usecase.exercise

import com.illera.peakprofit.domain.repository.ExerciseRepository
import javax.inject.Inject

class GetExerciseImageByIdUseCase @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) {
    suspend operator fun invoke(id: String): ByteArray? = exerciseRepository.getExerciseImageById(id)
}
