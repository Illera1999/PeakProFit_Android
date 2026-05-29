package com.illera.peakprofit.data.repository

import com.illera.peakprofit.BuildConfig
import com.illera.peakprofit.data.mapper.toDomainOrNull
import com.illera.peakprofit.data.remote.ExerciseDbApi
import com.illera.peakprofit.domain.entity.Exercise
import com.illera.peakprofit.domain.repository.ExerciseRepository

class ExerciseDbRepository(
    private val api: ExerciseDbApi
) : ExerciseRepository {
    private val detailCache = mutableMapOf<String, Exercise>()

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

    override suspend fun getExerciseById(id: String): Exercise {
        detailCache[id]?.let { return it }
        if (BuildConfig.RAPID_API_KEY.isBlank()) {
            throw IllegalStateException("RAPID_API_KEY no configurada")
        }
        val response = api.getExerciseById(id)
        val mapped = response.toDomainOrNull()
            ?: throw IllegalStateException("No se pudo mapear el detalle del ejercicio $id")
        detailCache[id] = mapped
        return mapped
    }
}
