package com.illera.peakprofit.domain.usecase.exercise

import com.illera.peakprofit.domain.repository.SavedExerciseRepository
import javax.inject.Inject

class UpdateSavedExerciseNoteUseCase @Inject constructor(
    private val savedExerciseRepository: SavedExerciseRepository
) {
    suspend operator fun invoke(userId: String, exerciseId: String, note: String) {
        savedExerciseRepository.updateSavedExerciseNote(
            userId = userId,
            exerciseId = exerciseId,
            note = note
        )
    }
}
