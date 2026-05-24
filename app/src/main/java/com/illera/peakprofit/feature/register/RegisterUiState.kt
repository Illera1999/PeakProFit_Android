package com.illera.peakprofit.feature.register

data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
