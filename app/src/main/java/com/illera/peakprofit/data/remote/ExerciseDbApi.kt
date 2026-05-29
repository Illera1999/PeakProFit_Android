package com.illera.peakprofit.data.remote

import com.illera.peakprofit.data.dto.ExerciseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ExerciseDbApi {
    @GET("exercises")
    suspend fun getExercises(
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0,
        @Query("sortMethod") sortMethod: String = "bodyPart",
        @Query("sortOrder") sortOrder: String = "ascending"
    ): List<ExerciseDto>

    @GET("exercises/exercise/{id}")
    suspend fun getExerciseById(
        @Path("id") id: String
    ): ExerciseDto
}
