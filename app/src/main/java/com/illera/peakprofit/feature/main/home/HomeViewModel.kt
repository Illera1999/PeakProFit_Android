package com.illera.peakprofit.feature.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illera.peakprofit.domain.entity.AuthState
import com.illera.peakprofit.domain.repository.AuthRepository
import com.illera.peakprofit.domain.usecase.auth.ObserveSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val observeSessionUseCase: ObserveSessionUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeSessionUseCase().collect { authState ->
                when (authState) {
                    is AuthState.Authenticated -> {
                        val session = authState.session
                        val displayName = session.email?.substringBefore("@")
                            ?.takeIf { it.isNotBlank() }
                            .orEmpty()
                        _uiState.value = _uiState.value.copy(
                            isAuthenticated = true,
                            isGuest = false,
                            userName = displayName,
                            userEmail = session.email ?: session.uid
                        )
                    }
                    AuthState.Guest -> {
                        _uiState.value = _uiState.value.copy(
                            isAuthenticated = false,
                            isGuest = true,
                            userName = "",
                            userEmail = ""
                        )
                    }
                    else -> {
                        _uiState.value = _uiState.value.copy(
                            isAuthenticated = false,
                            isGuest = false,
                            userName = "",
                            userEmail = ""
                        )
                    }
                }
            }
        }
    }

    fun logout() {
        authRepository.signOut()
    }
}
