package com.illera.peakprofit.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.illera.peakprofit.domain.entity.UserSession
import com.illera.peakprofit.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    private val _session = MutableStateFlow(firebaseAuth.currentUser?.toSession())
    override val session: StateFlow<UserSession?> = _session.asStateFlow()

    private val listener = FirebaseAuth.AuthStateListener { auth ->
        _session.value = auth.currentUser?.toSession()
    }

    init {
        firebaseAuth.addAuthStateListener(listener)
    }

    override suspend fun signIn(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun register(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

    private fun com.google.firebase.auth.FirebaseUser.toSession(): UserSession {
        return UserSession(uid = uid, email = email)
    }
}
