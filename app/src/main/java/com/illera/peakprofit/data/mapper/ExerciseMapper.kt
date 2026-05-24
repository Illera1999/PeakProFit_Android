package com.illera.peakprofit.data.mapper

import com.illera.peakprofit.data.dto.ExerciseDto
import com.illera.peakprofit.domain.entity.Exercise

fun ExerciseDto.toDomainOrNull(): Exercise? {
    val safeName = name?.trim().orEmpty()
    if (safeName.isBlank()) return null

    return Exercise(
        id = exerciseId?.trim().takeUnless { it.isNullOrBlank() } ?: safeName,
        name = safeName,
        gifUrl = gifUrl.orEmpty(),
        bodyParts = bodyParts.normalizeSingleOrList(bodyPart),
        targetMuscles = targetMuscles.normalizeSingleOrList(target),
        equipments = equipments.normalizeSingleOrList(equipment)
    )
}

private fun List<String>?.normalizeSingleOrList(singleValue: String?): List<String> {
    return when {
        !this.isNullOrEmpty() -> this
        !singleValue.isNullOrBlank() -> listOf(singleValue)
        else -> emptyList()
    }
}
