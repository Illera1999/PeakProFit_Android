package com.illera.peakprofit.feature.auth.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.illera.peakprofit.R
import com.illera.peakprofit.domain.entity.AuthState
import com.illera.peakprofit.feature.auth.components.AuthForm

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val authState by viewModel.authState.collectAsStateWithLifecycle()

    LaunchedEffect(authState) {
        if (authState is AuthState.Authenticated) onNavigateToHome()
    }

    AuthForm(
        title = stringResource(R.string.register_title),
        email = state.email,
        password = state.password,
        confirmPassword = state.confirmPassword,
        primaryActionText = stringResource(R.string.register_primary_action),
        secondaryActionText = stringResource(R.string.register_secondary_action),
        onEmailChanged = viewModel::onEmailChanged,
        onPasswordChanged = viewModel::onPasswordChanged,
        onConfirmPasswordChanged = viewModel::onConfirmPasswordChanged,
        onPrimaryAction = viewModel::register,
        onSecondaryAction = onNavigateToLogin,
        errorMessage = state.errorMessage,
        enabled = !state.isLoading,
        loading = state.isLoading
    )
}
