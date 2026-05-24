package com.illera.peakprofit.data.dto

data class ExerciseMetaDto(
    val total: Int? = null,
    val hasNextPage: Boolean? = null,
    val hasPreviousPage: Boolean? = null,
    val nextCursor: String? = null,
    val previousCursor: String? = null
)
