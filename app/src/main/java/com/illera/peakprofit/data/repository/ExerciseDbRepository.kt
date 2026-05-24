package com.illera.peakprofit.data.repository

import com.illera.peakprofit.data.remote.ExerciseDbApi
import com.illera.peakprofit.domain.entity.Exercise
import com.illera.peakprofit.domain.repository.ExerciseRepository

class ExerciseDbRepository(
    private val api: ExerciseDbApi
) : ExerciseRepository {

    override suspend fun getExercises(): List<Exercise> {
        val dtos = api.getExercises().data

        return dtos.mapNotNull { dto ->
            val name = dto.name?.trim().orEmpty()
            if (name.isBlank()) return@mapNotNull null

            val bodyParts = when {
                !dto.bodyParts.isNullOrEmpty() -> dto.bodyParts
                !dto.bodyPart.isNullOrBlank() -> listOf(dto.bodyPart)
                else -> emptyList()
            }
            val targetMuscles = when {
                !dto.targetMuscles.isNullOrEmpty() -> dto.targetMuscles
                !dto.target.isNullOrBlank() -> listOf(dto.target)
                else -> emptyList()
            }
            val equipments = when {
                !dto.equipments.isNullOrEmpty() -> dto.equipments
                !dto.equipment.isNullOrBlank() -> listOf(dto.equipment)
                else -> emptyList()
            }

            Exercise(
                id = dto.exerciseId?.trim().takeUnless { it.isNullOrBlank() } ?: name,
                name = name,
                gifUrl = dto.gifUrl.orEmpty(),
                bodyParts = bodyParts,
                targetMuscles = targetMuscles,
                equipments = equipments
            )
        }
    }
}
