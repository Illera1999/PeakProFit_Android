package com.illera.peakprofit.feature.auth.login

import com.illera.peakprofit.core.ui.UiText

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isSendingPasswordReset: Boolean = false,
    val successMessage: UiText? = null,
    val errorMessage: UiText? = null
)
