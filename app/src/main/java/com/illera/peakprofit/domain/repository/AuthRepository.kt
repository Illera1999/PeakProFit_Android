package com.illera.peakprofit.domain.repository

import com.illera.peakprofit.domain.entity.AuthState
import com.illera.peakprofit.domain.entity.UserSession
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val session: StateFlow<AuthState>
    suspend fun signIn(email: String, password: String)
    suspend fun register(email: String, password: String)
    fun continueAsGuest()
    fun signOut()
}
