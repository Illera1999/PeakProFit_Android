package com.illera.peakprofit.domain.repository

import com.illera.peakprofit.domain.entity.AuthState
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val session: StateFlow<AuthState>
    suspend fun signIn(email: String, password: String)
    suspend fun register(email: String, password: String)
    suspend fun sendPasswordResetEmail(email: String)
    fun continueAsGuest()
    fun signOut()
}
