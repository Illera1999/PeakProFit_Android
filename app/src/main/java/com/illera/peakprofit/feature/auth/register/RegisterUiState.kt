package com.illera.peakprofit.feature.auth.register

import com.illera.peakprofit.core.ui.UiText

data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null
)
