package com.illera.peakprofit.feature.home

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
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onLoggedOut: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.userEmail) {
        if (state.userEmail.isBlank()) onLoggedOut()
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
    }
}
