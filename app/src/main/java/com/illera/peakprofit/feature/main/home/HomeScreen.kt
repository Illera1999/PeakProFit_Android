package com.illera.peakprofit.feature.main.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.illera.peakprofit.R
import com.illera.peakprofit.feature.main.home.components.InfoCard

@Composable
fun HomeScreen(
    onLoggedOut: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onOpenSavedExercises: () -> Unit,
    onOpenSettings: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val spacingLarge = dimensionResource(R.dimen.spacing_large)
    val spacingMedium = dimensionResource(R.dimen.spacing_medium)

    LaunchedEffect(state.isGuest, state.userEmail) {
        if (!state.isGuest && state.userEmail.isBlank()) onLoggedOut()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacingLarge),
        verticalArrangement = Arrangement.spacedBy(spacingMedium)
    ) {
        val displayName = when {
            state.isGuest -> stringResource(R.string.home_guest_user)
            state.userName.isBlank() -> stringResource(R.string.home_default_user)
            else -> state.userName
        }

        Text(text = stringResource(R.string.home_greeting, displayName))
        Text(text = state.userEmail)
        InfoCard(
            title = stringResource(R.string.home_next_session),
            value = state.nextWorkout.ifBlank { stringResource(R.string.home_next_session_placeholder) }
        )
        InfoCard(
            title = stringResource(R.string.home_streak),
            value = stringResource(R.string.home_streak_days, state.streakDays)
        )
        if (state.isAuthenticated) {
            Button(onClick = onOpenSavedExercises) {
                Text(text = stringResource(R.string.home_saved_exercises))
            }
        }
        Button(onClick = onOpenSettings) {
            Text(text = stringResource(R.string.home_open_settings))
        }
        if (state.isGuest) {
            Button(
                onClick = {
                    viewModel.logout()
                    onNavigateToLogin()
                }
            ) {
                Text(text = stringResource(R.string.home_sign_in))
            }
        } else {
            Button(onClick = viewModel::logout) {
                Text(text = stringResource(R.string.home_sign_out))
            }
        }
    }
}
