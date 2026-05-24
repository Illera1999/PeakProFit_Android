package com.illera.peakprofit.data.dto

import com.google.gson.annotations.SerializedName

data class ExerciseDto(
    @SerializedName(value = "exerciseId", alternate = ["id"])
    val exerciseId: String? = null,
    val name: String? = null,
    @SerializedName(value = "gifUrl", alternate = ["imageUrl"])
    val gifUrl: String? = null,
    @SerializedName("bodyParts")
    val bodyParts: List<String>? = null,
    @SerializedName("bodyPart")
    val bodyPart: String? = null,
    @SerializedName("targetMuscles")
    val targetMuscles: List<String>? = null,
    @SerializedName("target")
    val target: String? = null,
    @SerializedName("equipments")
    val equipments: List<String>? = null,
    @SerializedName("equipment")
    val equipment: String? = null,
    val secondaryMuscles: List<String>? = null,
    val keywords: List<String>? = null,
    val exerciseType: String? = null
)
