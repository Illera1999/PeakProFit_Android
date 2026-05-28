package com.illera.peakprofit.data.repository

import com.illera.peakprofit.BuildConfig
import com.illera.peakprofit.data.mapper.toDomainOrNull
import com.illera.peakprofit.data.remote.ExerciseDbApi
import com.illera.peakprofit.domain.entity.Exercise
import com.illera.peakprofit.domain.repository.ExerciseRepository

class ExerciseDbRepository(
    private val api: ExerciseDbApi
) : ExerciseRepository {

    override suspend fun getExercises(limit: Int, offset: Int): List<Exercise> {
        if (BuildConfig.RAPID_API_KEY.isBlank()) {
            throw IllegalStateException("RAPID_API_KEY no configurada")
        }
        val response = api.getExercises(
            limit = limit,
            offset = offset,
            sortMethod = "bodyPart",
            sortOrder = "ascending"
        )
        return response.mapNotNull { it.toDomainOrNull() }
    }
}
