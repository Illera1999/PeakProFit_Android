package com.illera.peakprofit.data.repository

import com.illera.peakprofit.BuildConfig
import com.illera.peakprofit.data.mapper.toDomainOrNull
import com.illera.peakprofit.data.remote.ExerciseDbApi
import com.illera.peakprofit.domain.entity.Exercise
import com.illera.peakprofit.domain.repository.ExerciseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.URL

class ExerciseDbRepository(
    private val api: ExerciseDbApi
) : ExerciseRepository {
    private val exerciseCache = mutableMapOf<String, Exercise>()
    private val imageCache = mutableMapOf<String, ByteArray>()
    private val imageResolution = 360

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
        return response.mapNotNull { dto ->
            dto.toDomainOrNull()?.also { exercise ->
                exerciseCache[exercise.id] = mergeExercise(exerciseCache[exercise.id], exercise)
            }
        }
    }

    override suspend fun getExerciseById(id: String): Exercise {
        exerciseCache[id]?.takeIf { hasDetailData(it) }?.let { return it }
        if (BuildConfig.RAPID_API_KEY.isBlank()) {
            throw IllegalStateException("RAPID_API_KEY no configurada")
        }
        val response = api.getExerciseById(id)
        val mapped = response.toDomainOrNull()
            ?: throw IllegalStateException("No se pudo mapear el detalle del ejercicio $id")
        val merged = mergeExercise(exerciseCache[id], mapped)
        exerciseCache[id] = merged
        return merged
    }

    override suspend fun getExerciseImageById(id: String): ByteArray? {
        imageCache[id]?.let { return it }
        if (BuildConfig.RAPID_API_KEY.isBlank()) {
            throw IllegalStateException("RAPID_API_KEY no configurada")
        }
        val imageBytes = runCatching {
            api.getExerciseImage(id, resolution = imageResolution).bytes()
        }.recoverCatching { error ->
            if (error is HttpException && error.code() == 404) {
                val exercise = exerciseCache[id] ?: getExerciseById(id)
                val imageUrl = exercise.gifUrl.takeIf { it.isNotBlank() } ?: return null
                withContext(Dispatchers.IO) {
                    URL(imageUrl).openStream().use { it.readBytes() }
                }
            } else {
                throw error
            }
        }.getOrNull() ?: return null

        if (imageBytes.isEmpty()) {
            return null
        }
        imageCache[id] = imageBytes
        return imageBytes
    }

    private fun hasDetailData(exercise: Exercise): Boolean {
        return exercise.instructions.isNotEmpty() ||
            exercise.description.isNotBlank() ||
            exercise.difficulty.isNotBlank() ||
            exercise.category.isNotBlank() ||
            exercise.secondaryMuscles.isNotEmpty()
    }

    private fun mergeExercise(cached: Exercise?, incoming: Exercise): Exercise {
        if (cached == null) return incoming

        return incoming.copy(
            gifUrl = incoming.gifUrl.ifBlank { cached.gifUrl },
            bodyParts = incoming.bodyParts.ifEmpty { cached.bodyParts },
            targetMuscles = incoming.targetMuscles.ifEmpty { cached.targetMuscles },
            equipments = incoming.equipments.ifEmpty { cached.equipments },
            secondaryMuscles = incoming.secondaryMuscles.ifEmpty { cached.secondaryMuscles },
            instructions = incoming.instructions.ifEmpty { cached.instructions },
            description = incoming.description.ifBlank { cached.description },
            difficulty = incoming.difficulty.ifBlank { cached.difficulty },
            category = incoming.category.ifBlank { cached.category }
        )
    }
}
