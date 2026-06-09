package com.illera.peakprofit.feature.auth.register

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.illera.peakprofit.R
import com.illera.peakprofit.core.ui.UiText
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

    fun onConfirmPasswordChanged(value: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = value)
    }

    fun register() {
        val email = _uiState.value.email.trim()
        val password = _uiState.value.password
        val confirmPassword = _uiState.value.confirmPassword
        val validationError = validateCredentials(
            email = email,
            password = password,
            confirmPassword = confirmPassword
        )
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

    private fun validateCredentials(email: String, password: String, confirmPassword: String): UiText? {
        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            return UiText.StringResource(R.string.auth_error_required_register_fields)
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return UiText.StringResource(R.string.auth_error_invalid_email)
        }
        if (password.length < 6) {
            return UiText.StringResource(R.string.auth_error_short_password)
        }
        if (password != confirmPassword) {
            return UiText.StringResource(R.string.auth_error_passwords_do_not_match)
        }
        return null
    }

    private fun mapAuthError(throwable: Throwable): UiText {
        return when (throwable) {
            is FirebaseAuthInvalidCredentialsException -> UiText.StringResource(R.string.auth_error_invalid_email)
            is FirebaseNetworkException -> UiText.StringResource(R.string.auth_error_network)
            is FirebaseAuthException -> when (throwable.errorCode) {
                "ERROR_EMAIL_ALREADY_IN_USE" -> UiText.StringResource(R.string.auth_error_email_in_use)
                "ERROR_WEAK_PASSWORD" -> UiText.StringResource(R.string.auth_error_weak_password)
                "ERROR_OPERATION_NOT_ALLOWED" -> UiText.StringResource(R.string.auth_error_register_disabled)
                "ERROR_TOO_MANY_REQUESTS" -> UiText.StringResource(R.string.auth_error_too_many_requests)
                else -> UiText.StringResource(R.string.auth_error_register_generic)
            }
            else -> UiText.StringResource(R.string.auth_error_register_generic)
        }
    }
}
