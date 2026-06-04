package com.illera.peakprofit.feature.main.home

data class HomeUiState(
    val isAuthenticated: Boolean = false,
    val isGuest: Boolean = false,
    val userName: String = "Usuario",
    val userEmail: String = "",
    val nextWorkout: String = "Push - Fuerza",
    val streakDays: Int = 4
)
