package com.illera.peakprofit.feature.auth.login

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.illera.peakprofit.core.ui.ConfirmDialog
import com.illera.peakprofit.domain.entity.AuthState

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
            title = "Salir de la app",
            message = "¿Seguro que quieres salir?",
            confirmText = "Salir",
            dismissText = "Cancelar",
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Iniciar sesion")
        OutlinedTextField(
            value = state.email,
            onValueChange = viewModel::onEmailChanged,
            label = { Text("Email") },
            enabled = !state.isLoading
        )
        OutlinedTextField(
            value = state.password,
            onValueChange = viewModel::onPasswordChanged,
            label = { Text("Password") },
            enabled = !state.isLoading
        )
        Button(onClick = viewModel::signIn, enabled = !state.isLoading) {
            Text("Entrar")
        }
        Button(onClick = onNavigateToRegister, enabled = !state.isLoading) {
            Text("No tienes cuenta? Registrate")
        }
        Button(onClick = viewModel::continueAsGuest, enabled = !state.isLoading) {
            Text("Entrar como invitado")
        }
        state.errorMessage?.let { message ->
            Text(text = message)
        }
    }
}
