package com.illera.peakprofit.feature.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.illera.peakprofit.domain.entity.AuthState
import com.illera.peakprofit.domain.usecase.auth.ObserveSessionUseCase
import com.illera.peakprofit.domain.usecase.auth.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val observeSessionUseCase: ObserveSessionUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        viewModelScope.launch {
            observeSessionUseCase().collect { currentSession ->
                _authState.value = currentSession
            }
        }
    }

    fun onEmailChanged(value: String) {
        _uiState.value = _uiState.value.copy(email = value)
    }

    fun onPasswordChanged(value: String) {
        _uiState.value = _uiState.value.copy(password = value)
    }

    fun register() {
        val email = _uiState.value.email.trim()
        val password = _uiState.value.password
        val validationError = validateCredentials(email = email, password = password)
        if (validationError != null) {
            _uiState.value = _uiState.value.copy(errorMessage = validationError)
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            runCatching {
                registerUseCase(email, password)
            }.onFailure {
                _uiState.value = _uiState.value.copy(errorMessage = mapAuthError(it))
            }
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    private fun validateCredentials(email: String, password: String): String? {
        if (email.isBlank() || password.isBlank()) return "Email y password son obligatorios"
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) return "Email no valido"
        if (password.length < 6) return "La password debe tener al menos 6 caracteres"
        return null
    }

    private fun mapAuthError(throwable: Throwable): String {
        return when (throwable) {
            is FirebaseAuthInvalidCredentialsException -> "Email no valido"
            is FirebaseNetworkException -> "Sin conexion. Revisa internet e intentalo de nuevo"
            is FirebaseAuthException -> when (throwable.errorCode) {
                "ERROR_EMAIL_ALREADY_IN_USE" -> "Ese email ya esta registrado"
                "ERROR_WEAK_PASSWORD" -> "La password es demasiado debil"
                "ERROR_OPERATION_NOT_ALLOWED" -> "Registro no permitido temporalmente"
                "ERROR_TOO_MANY_REQUESTS" -> "Demasiados intentos. Espera y vuelve a intentar"
                else -> "No se pudo crear la cuenta"
            }
            else -> "No se pudo crear la cuenta"
        }
    }
}
