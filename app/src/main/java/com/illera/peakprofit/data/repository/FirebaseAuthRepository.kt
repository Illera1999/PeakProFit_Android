package com.illera.peakprofit.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.illera.peakprofit.domain.entity.AuthState
import com.illera.peakprofit.domain.entity.UserSession
import com.illera.peakprofit.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    private val _session = MutableStateFlow<AuthState>(AuthState.Loading)
    override val session: StateFlow<AuthState> = _session.asStateFlow()
    private var isGuestSession = false

    private val listener = FirebaseAuth.AuthStateListener { auth ->
        _session.value = when {
            auth.currentUser != null -> {
                isGuestSession = false
                AuthState.Authenticated(auth.currentUser!!.toSession())
            }
            isGuestSession -> AuthState.Guest
            else -> AuthState.Unauthenticated
        }
    }

    init {
        firebaseAuth.addAuthStateListener(listener)
    }

    override suspend fun signIn(email: String, password: String) {
        isGuestSession = false
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun register(email: String, password: String) {
        isGuestSession = false
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
    }

    override fun continueAsGuest() {
        isGuestSession = true
        if (firebaseAuth.currentUser == null) {
            _session.value = AuthState.Guest
        } else {
            _session.value = AuthState.Authenticated(firebaseAuth.currentUser!!.toSession())
        }
    }

    override fun signOut() {
        isGuestSession = false
        firebaseAuth.signOut()
    }

    private fun com.google.firebase.auth.FirebaseUser.toSession(): UserSession {
        return UserSession(uid = uid, email = email)
    }
}
