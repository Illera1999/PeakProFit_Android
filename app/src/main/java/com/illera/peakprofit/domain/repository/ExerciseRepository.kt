package com.illera.peakprofit.domain.repository

import com.illera.peakprofit.domain.entity.Exercise

interface ExerciseRepository {
    suspend fun getExercises(limit: Int, offset: Int): List<Exercise>
}
