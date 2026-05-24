package com.illera.peakprofit.data.dto

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class ExerciseListResponseDeserializer : JsonDeserializer<ExerciseListResponseDto> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext
    ): ExerciseListResponseDto {
        if (json == null || json.isJsonNull) return ExerciseListResponseDto()

        val listType = object : TypeToken<List<ExerciseDto>>() {}.type

        if (json.isJsonArray) {
            val items: List<ExerciseDto> = context.deserialize(json, listType) ?: emptyList()
            return ExerciseListResponseDto(data = items)
        }

        if (json.isJsonObject) {
            val root: JsonObject = json.asJsonObject
            val dataElement = root.get("data")
            val items: List<ExerciseDto> = when {
                dataElement != null && dataElement.isJsonArray -> context.deserialize(dataElement, listType) ?: emptyList()
                else -> emptyList()
            }
            return ExerciseListResponseDto(
                success = root.get("success")?.asBoolean,
                meta = context.deserialize(root.get("meta"), ExerciseMetaDto::class.java),
                data = items
            )
        }

        return ExerciseListResponseDto()
    }
}
