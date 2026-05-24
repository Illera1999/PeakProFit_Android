package com.illera.peakprofit.data.remote

import com.illera.peakprofit.data.dto.ExerciseListResponseDto
import retrofit2.http.GET

interface ExerciseDbApi {
    @GET("api/v1/exercises")
    suspend fun getExercises(): ExerciseListResponseDto
}
