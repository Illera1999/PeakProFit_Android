package com.illera.peakprofit.domain.repository

import com.illera.peakprofit.domain.entity.Exercise

interface ExerciseRepository {
    suspend fun getExercises(limit: Int, offset: Int): List<Exercise>
    suspend fun getExerciseById(id: String): Exercise
    suspend fun getExerciseImageById(id: String): ByteArray?
}
