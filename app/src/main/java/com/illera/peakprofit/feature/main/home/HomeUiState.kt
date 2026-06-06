package com.illera.peakprofit.feature.main.home

data class HomeUiState(
    val isAuthenticated: Boolean = false,
    val isGuest: Boolean = false,
    val userName: String = "",
    val userEmail: String = "",
    val nextWorkout: String = "",
    val streakDays: Int = 4
)
