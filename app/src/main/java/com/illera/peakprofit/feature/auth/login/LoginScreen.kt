package com.illera.peakprofit.feature.auth.login

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.illera.peakprofit.R
import com.illera.peakprofit.core.ui.ConfirmDialog
import com.illera.peakprofit.domain.entity.AuthState
import com.illera.peakprofit.feature.auth.components.AuthForm

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val authState by viewModel.authState.collectAsStateWithLifecycle()
    val activity = LocalActivity.current
    var showExitDialog by remember { mutableStateOf(false) }

    LaunchedEffect(authState) {
        if (authState is AuthState.Authenticated || authState is AuthState.Guest) onNavigateToHome()
    }

    if (showExitDialog) {
        ConfirmDialog(
            title = stringResource(R.string.exit_dialog_title),
            message = stringResource(R.string.exit_dialog_message),
            confirmText = stringResource(R.string.exit_dialog_confirm),
            dismissText = stringResource(R.string.exit_dialog_dismiss),
            onConfirm = {
                showExitDialog = false
                activity?.finish()
            },
            onDismiss = { showExitDialog = false }
        )
    }

    BackHandler {
        showExitDialog = true
    }

    AuthForm(
        title = stringResource(R.string.login_title),
        email = state.email,
        password = state.password,
        primaryActionText = stringResource(R.string.login_primary_action),
        secondaryActionText = stringResource(R.string.login_secondary_action),
        tertiaryActionText = stringResource(R.string.login_guest_action),
        supportingText = stringResource(R.string.login_password_help),
        onEmailChanged = viewModel::onEmailChanged,
        onPasswordChanged = viewModel::onPasswordChanged,
        onPrimaryAction = viewModel::signIn,
        onSecondaryAction = onNavigateToRegister,
        onTertiaryAction = viewModel::continueAsGuest,
        errorMessage = state.errorMessage,
        enabled = !state.isLoading,
        loading = state.isLoading
    )
}
