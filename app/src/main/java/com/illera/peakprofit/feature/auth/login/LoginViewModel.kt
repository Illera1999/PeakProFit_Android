package com.illera.peakprofit.feature.auth.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.illera.peakprofit.R
import com.illera.peakprofit.core.ui.UiText
import com.illera.peakprofit.domain.entity.AuthState
import com.illera.peakprofit.domain.usecase.auth.ContinueAsGuestUseCase
import com.illera.peakprofit.domain.usecase.auth.ObserveSessionUseCase
import com.illera.peakprofit.domain.usecase.auth.SendPasswordResetEmailUseCase
import com.illera.peakprofit.domain.usecase.auth.SignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val sendPasswordResetEmailUseCase: SendPasswordResetEmailUseCase,
    private val continueAsGuestUseCase: ContinueAsGuestUseCase,
    private val observeSessionUseCase: ObserveSessionUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

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
        _uiState.value = _uiState.value.copy(
            email = value,
            successMessage = null,
            errorMessage = null
        )
    }

    fun onPasswordChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            password = value,
            errorMessage = null
        )
    }

    fun signIn() {
        val email = _uiState.value.email.trim()
        val password = _uiState.value.password
        val validationError = validateCredentials(email = email, password = password)
        if (validationError != null) {
            _uiState.value = _uiState.value.copy(
                successMessage = null,
                errorMessage = validationError
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                successMessage = null,
                errorMessage = null
            )
            runCatching {
                signInUseCase(email, password)
            }.onFailure {
                _uiState.value = _uiState.value.copy(errorMessage = mapAuthError(it))
            }
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun sendPasswordResetEmail() {
        val email = _uiState.value.email.trim()
        val validationError = validateEmail(email)
        if (validationError != null) {
            _uiState.value = _uiState.value.copy(
                successMessage = null,
                errorMessage = validationError
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSendingPasswordReset = true,
                successMessage = null,
                errorMessage = null
            )
            runCatching {
                sendPasswordResetEmailUseCase(email)
            }.onSuccess {
                _uiState.value = _uiState.value.copy(
                    successMessage = UiText.StringResource(
                        id = R.string.auth_password_reset_sent,
                        formatArgs = listOf(email)
                    )
                )
            }.onFailure {
                _uiState.value = _uiState.value.copy(errorMessage = mapPasswordResetError(it))
            }
            _uiState.value = _uiState.value.copy(isSendingPasswordReset = false)
        }
    }

    fun continueAsGuest() {
        continueAsGuestUseCase()
    }

    private fun validateCredentials(email: String, password: String): UiText? {
        if (email.isBlank()) {
            return UiText.StringResource(R.string.auth_error_required_fields)
        }
        validateEmail(email)?.let { return it }
        if (password.isBlank()) {
            return UiText.StringResource(R.string.auth_error_required_fields)
        }
        if (password.length < 6) {
            return UiText.StringResource(R.string.auth_error_short_password)
        }
        return null
    }

    private fun validateEmail(email: String): UiText? {
        if (email.isBlank()) {
            return UiText.StringResource(R.string.auth_error_email_required)
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return UiText.StringResource(R.string.auth_error_invalid_email)
        }
        return null
    }

    private fun mapAuthError(throwable: Throwable): UiText {
        return when (throwable) {
            is FirebaseAuthInvalidUserException -> UiText.StringResource(R.string.auth_error_user_not_found)
            is FirebaseAuthInvalidCredentialsException -> UiText.StringResource(R.string.auth_error_invalid_credentials)
            is FirebaseNetworkException -> UiText.StringResource(R.string.auth_error_network)
            is FirebaseAuthException -> when (throwable.errorCode) {
                "ERROR_USER_DISABLED" -> UiText.StringResource(R.string.auth_error_user_disabled)
                "ERROR_TOO_MANY_REQUESTS" -> UiText.StringResource(R.string.auth_error_too_many_requests)
                else -> UiText.StringResource(R.string.auth_error_sign_in_generic)
            }
            else -> UiText.StringResource(R.string.auth_error_sign_in_generic)
        }
    }

    private fun mapPasswordResetError(throwable: Throwable): UiText {
        return when (throwable) {
            is FirebaseAuthInvalidUserException -> UiText.StringResource(R.string.auth_error_user_not_found)
            is FirebaseAuthInvalidCredentialsException -> UiText.StringResource(R.string.auth_error_invalid_email)
            is FirebaseNetworkException -> UiText.StringResource(R.string.auth_error_network)
            is FirebaseAuthException -> when (throwable.errorCode) {
                "ERROR_USER_DISABLED" -> UiText.StringResource(R.string.auth_error_user_disabled)
                "ERROR_TOO_MANY_REQUESTS" -> UiText.StringResource(R.string.auth_error_too_many_requests)
                else -> UiText.StringResource(R.string.auth_error_password_reset_generic)
            }
            else -> UiText.StringResource(R.string.auth_error_password_reset_generic)
        }
    }
}
