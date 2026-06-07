package com.illera.peakprofit.domain.repository

import com.illera.peakprofit.domain.entity.Exercise
import kotlinx.coroutines.flow.Flow

interface SavedExerciseRepository {
    suspend fun saveExercise(userId: String, exercise: Exercise)
    suspend fun updateSavedExerciseNote(userId: String, exerciseId: String, note: String)
    suspend fun removeSavedExercise(userId: String, exerciseId: String)
    suspend fun getSavedExerciseById(userId: String, exerciseId: String): Exercise?
    fun observeSavedExercises(userId: String): Flow<List<Exercise>>
    fun isExerciseSaved(userId: String, exerciseId: String): Flow<Boolean>
}
