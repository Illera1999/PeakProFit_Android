package com.illera.peakprofit.domain.entity

data class Exercise(
    val id: String,
    val name: String,
    val gifUrl: String,
    val bodyParts: List<String>,
    val targetMuscles: List<String>,
    val equipments: List<String>,
    val secondaryMuscles: List<String>,
    val instructions: List<String>,
    val description: String,
    val difficulty: String,
    val category: String
)
