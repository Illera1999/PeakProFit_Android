package com.illera.peakprofit.feature.home

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.illera.peakprofit.core.ui.ConfirmDialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onNavigateToExercises: () -> Unit,
    onLoggedOut: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val activity = LocalContext.current as? Activity
    var showExitDialog by remember { mutableStateOf(false) }

    BackHandler {
        showExitDialog = true
    }

    LaunchedEffect(state.userEmail) {
        if (state.userEmail.isBlank()) onLoggedOut()
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "Hola, ${state.userName}")
        Text(text = state.userEmail)
        Card {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = "Siguiente sesión")
                Text(text = state.nextWorkout)
            }
        }
        Card {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = "Racha")
                Text(text = "${state.streakDays} días")
            }
        }
        Button(onClick = viewModel::logout) {
            Text(text = "Cerrar sesión")
        }
        Button(onClick = onNavigateToExercises) {
            Text(text = "Ver ejercicios")
        }
    }
}
