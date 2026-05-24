package com.illera.peakprofit.feature.home

data class HomeUiState(
    val userName: String = "Usuario",
    val userEmail: String = "",
    val nextWorkout: String = "Push - Fuerza",
    val streakDays: Int = 4
)
