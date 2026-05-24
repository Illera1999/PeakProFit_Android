package com.illera.peakprofit.feature.register

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.illera.peakprofit.domain.entity.AuthState

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Registrarse")
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
        Button(onClick = viewModel::register, enabled = !state.isLoading) {
            Text("Crear cuenta")
        }
        Button(onClick = onNavigateToLogin, enabled = !state.isLoading) {
            Text("Ya tienes cuenta? Inicia sesion")
        }
        state.errorMessage?.let { message ->
            Text(text = message)
        }
    }
}
