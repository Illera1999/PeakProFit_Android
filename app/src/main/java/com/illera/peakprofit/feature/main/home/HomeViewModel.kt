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
                val session = (authState as? AuthState.Authenticated)?.session
                val displayName = session?.email?.substringBefore("@")
                    ?.takeIf { it.isNotBlank() }
                    ?: "Usuario"
                _uiState.value = _uiState.value.copy(
                    userName = displayName,
                    userEmail = session?.email ?: session?.uid.orEmpty()
                )
            }
        }
    }

    fun logout() {
        authRepository.signOut()
    }
}
