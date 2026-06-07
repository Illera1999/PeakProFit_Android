package com.illera.peakprofit.feature.main.home

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.illera.peakprofit.R
import com.illera.peakprofit.core.ui.ConfirmDialog

@Composable
fun HomeScreen(
    onLoggedOut: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onOpenSavedExercises: () -> Unit,
    onOpenSettings: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var showDeleteAudioDialog by remember { mutableStateOf(false) }
    val spacingLarge = dimensionResource(R.dimen.spacing_large)
    val spacingMedium = dimensionResource(R.dimen.spacing_medium)
    val recordAudioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            viewModel.startRecording()
        } else {
            viewModel.onRecordPermissionDenied()
        }
    }

    LaunchedEffect(state.isGuest, state.userEmail) {
        if (!state.isGuest && state.userEmail.isBlank()) onLoggedOut()
    }

    if (showDeleteAudioDialog) {
        ConfirmDialog(
            title = stringResource(R.string.home_motivational_audio_delete_title),
            message = stringResource(R.string.home_motivational_audio_delete_message),
            confirmText = stringResource(R.string.home_motivational_audio_delete_confirm),
            dismissText = stringResource(R.string.exit_dialog_dismiss),
            onConfirm = {
                showDeleteAudioDialog = false
                viewModel.deleteMotivationalAudio()
            },
            onDismiss = { showDeleteAudioDialog = false }
        )
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
        if (state.isAuthenticated) {
            Card {
                Column(
                    modifier = Modifier.padding(spacingMedium),
                    verticalArrangement = Arrangement.spacedBy(spacingMedium)
                ) {
                    Text(text = stringResource(R.string.home_motivational_audio_title))
                    Text(text = state.audioStatusMessage)
                    if (state.isRecordingMotivationalAudio) {
                        Text(
                            text = stringResource(
                                R.string.home_motivational_audio_recording_time,
                                state.recordingSecondsRemaining
                            )
                        )
                    }
                    Button(
                        onClick = {
                            if (state.isRecordingMotivationalAudio) {
                                viewModel.stopRecording()
                            } else {
                                val hasPermission = ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.RECORD_AUDIO
                                ) == PackageManager.PERMISSION_GRANTED
                                if (hasPermission) {
                                    viewModel.startRecording()
                                } else {
                                    recordAudioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                }
                            }
                        }
                    ) {
                        Text(
                            text = when {
                                state.isRecordingMotivationalAudio ->
                                    stringResource(R.string.home_motivational_audio_stop_recording)
                                state.hasMotivationalAudio ->
                                    stringResource(R.string.home_motivational_audio_rerecord)
                                else ->
                                    stringResource(R.string.home_motivational_audio_record)
                            }
                        )
                    }
                    if (state.hasMotivationalAudio) {
                        Button(
                            onClick = viewModel::togglePlayback,
                            enabled = !state.isRecordingMotivationalAudio
                        ) {
                            Text(
                                text = if (state.isPlayingMotivationalAudio) {
                                    stringResource(R.string.home_motivational_audio_stop_playback)
                                } else {
                                    stringResource(R.string.home_motivational_audio_play)
                                }
                            )
                        }
                        Button(
                            onClick = { showDeleteAudioDialog = true },
                            enabled = !state.isRecordingMotivationalAudio
                        ) {
                            Text(text = stringResource(R.string.home_motivational_audio_delete))
                        }
                    }
                }
            }
        }
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
