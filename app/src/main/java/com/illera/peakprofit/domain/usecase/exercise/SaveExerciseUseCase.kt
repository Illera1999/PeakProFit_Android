package com.illera.peakprofit.domain.usecase.exercise

import com.illera.peakprofit.domain.entity.Exercise
import com.illera.peakprofit.domain.repository.SavedExerciseRepository
import javax.inject.Inject

class SaveExerciseUseCase @Inject constructor(
    private val savedExerciseRepository: SavedExerciseRepository
) {
    suspend operator fun invoke(userId: String, exercise: Exercise) {
        savedExerciseRepository.saveExercise(userId = userId, exercise = exercise)
    }
}
