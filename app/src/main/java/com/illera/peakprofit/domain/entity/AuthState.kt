package com.illera.peakprofit.domain.entity

sealed interface AuthState {
    data object Loading : AuthState
    data class Authenticated(val session: UserSession) : AuthState
    data object Guest : AuthState
    data object Unauthenticated : AuthState
    data class Error(val message: String) : AuthState
}
