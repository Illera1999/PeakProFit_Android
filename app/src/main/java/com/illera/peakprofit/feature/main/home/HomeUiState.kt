package com.illera.peakprofit.feature.main.home

import com.illera.peakprofit.core.ui.UiText

data class HomeUiState(
    val isAuthenticated: Boolean = false,
    val isGuest: Boolean = false,
    val userName: String = "",
    val userEmail: String = "",
    val nextWorkout: String = "",
    val streakDays: Int = 4,
    val hasMotivationalAudio: Boolean = false,
    val isRecordingMotivationalAudio: Boolean = false,
    val isPlayingMotivationalAudio: Boolean = false,
    val recordingSecondsRemaining: Int = 60,
    val audioStatusMessage: UiText? = null
)
