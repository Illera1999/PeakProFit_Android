package com.illera.peakprofit.feature.main.profile

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.illera.peakprofit.R
import com.illera.peakprofit.core.theme.PeakTheme
import com.illera.peakprofit.core.theme.body
import com.illera.peakprofit.core.ui.ConfirmDialog
import com.illera.peakprofit.core.ui.asString
import com.illera.peakprofit.core.ui.components.PeakBodyText
import com.illera.peakprofit.core.ui.components.PeakMutedText
import com.illera.peakprofit.core.ui.components.PeakPrimaryButton
import com.illera.peakprofit.core.ui.components.PeakScreenTitle
import com.illera.peakprofit.core.ui.components.PeakSectionCard
import com.illera.peakprofit.core.ui.components.PeakTextButton

@Composable
fun ProfileScreen(
    onLoggedOut: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onOpenSavedExercises: () -> Unit,
    onOpenSettings: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var showDeleteAudioDialog by remember { mutableStateOf(false) }
    val spacing = PeakTheme.spacing
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
            .background(PeakTheme.colors.canvas)
            .safeDrawingPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = spacing.large, vertical = spacing.large),
            contentAlignment = Alignment.Center
        ) {
            PeakScreenTitle(text = stringResource(R.string.tab_profile))
        }
        HorizontalDivider(color = PeakTheme.colors.divider)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = spacing.large, vertical = spacing.large),
            verticalArrangement = Arrangement.spacedBy(spacing.medium)
        ) {
            ProfileAccountCard(state = state)

            if (state.isAuthenticated) {
                ProfileActionCard(
                    icon = Icons.Default.Bookmark,
                    title = stringResource(R.string.saved_exercises_title),
                    subtitle = stringResource(R.string.profile_saved_exercises_description),
                    onClick = onOpenSavedExercises
                )
                ProfileAudioCard(
                    state = state,
                    onRequestRecord = {
                        val hasPermission = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.RECORD_AUDIO
                        ) == PackageManager.PERMISSION_GRANTED
                        if (hasPermission) {
                            viewModel.startRecording()
                        } else {
                            recordAudioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                        }
                    },
                    onStopRecording = viewModel::stopRecording,
                    onTogglePlayback = viewModel::togglePlayback,
                    onDeleteAudio = { showDeleteAudioDialog = true }
                )
            } else {
                PeakSectionCard {
                    Text(
                        text = stringResource(R.string.profile_guest_description),
                        style = MaterialTheme.typography.body,
                        color = PeakTheme.colors.textSecondary
                    )
                }
            }

            ProfileActionCard(
                icon = Icons.Default.Settings,
                title = stringResource(R.string.settings_title),
                subtitle = stringResource(R.string.profile_settings_description),
                onClick = onOpenSettings
            )

            PeakPrimaryButton(
                text = if (state.isGuest) {
                    stringResource(R.string.home_sign_in)
                } else {
                    stringResource(R.string.home_sign_out)
                },
                onClick = {
                    if (state.isGuest) {
                        viewModel.logout()
                        onNavigateToLogin()
                    } else {
                        viewModel.logout()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ProfileAccountCard(state: ProfileUiState) {
    val displayName = when {
        state.isGuest -> stringResource(R.string.home_guest_user)
        state.userName.isBlank() -> stringResource(R.string.home_default_user)
        else -> state.userName
    }

    PeakSectionCard {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(PeakTheme.spacing.medium)
        ) {
            androidx.compose.material3.Surface(
                shape = PeakTheme.shapes.field,
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Box(
                    modifier = Modifier.padding(PeakTheme.spacing.medium),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = PeakTheme.colors.textSecondary
                    )
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(PeakTheme.spacing.xxSmall)) {
                Text(
                    text = displayName,
                    style = MaterialTheme.typography.titleMedium,
                    color = PeakTheme.colors.textPrimary
                )
                Text(
                    text = if (state.isGuest) {
                        stringResource(R.string.profile_guest_label)
                    } else {
                        state.userEmail
                    },
                    style = MaterialTheme.typography.body,
                    color = PeakTheme.colors.textSecondary
                )
            }
        }
    }
}

@Composable
private fun ProfileActionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    PeakSectionCard(onClick = onClick) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(PeakTheme.spacing.medium)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(PeakTheme.spacing.xxSmall)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = PeakTheme.colors.textPrimary
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.body,
                    color = PeakTheme.colors.textSecondary
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = PeakTheme.colors.textSecondary
            )
        }
    }
}

@Composable
private fun ProfileAudioCard(
    state: ProfileUiState,
    onRequestRecord: () -> Unit,
    onStopRecording: () -> Unit,
    onTogglePlayback: () -> Unit,
    onDeleteAudio: () -> Unit
) {
    PeakSectionCard {
        Column(verticalArrangement = Arrangement.spacedBy(PeakTheme.spacing.small)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(PeakTheme.spacing.small)
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = stringResource(R.string.home_motivational_audio_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = PeakTheme.colors.textPrimary
                )
            }

            state.audioStatusMessage?.let { status ->
                PeakMutedText(text = status.asString())
            }

            if (state.isRecordingMotivationalAudio) {
                PeakBodyText(
                    text = stringResource(
                        R.string.home_motivational_audio_recording_time,
                        state.recordingSecondsRemaining
                    )
                )
            }

            PeakPrimaryButton(
                text = when {
                    state.isRecordingMotivationalAudio ->
                        stringResource(R.string.home_motivational_audio_stop_recording)
                    state.hasMotivationalAudio ->
                        stringResource(R.string.home_motivational_audio_rerecord)
                    else ->
                        stringResource(R.string.home_motivational_audio_record)
                },
                onClick = {
                    if (state.isRecordingMotivationalAudio) {
                        onStopRecording()
                    } else {
                        onRequestRecord()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            if (state.hasMotivationalAudio) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PeakTextButton(
                        text = if (state.isPlayingMotivationalAudio) {
                            stringResource(R.string.home_motivational_audio_stop_playback)
                        } else {
                            stringResource(R.string.home_motivational_audio_play)
                        },
                        onClick = onTogglePlayback
                    )
                    PeakTextButton(
                        text = stringResource(R.string.home_motivational_audio_delete),
                        onClick = onDeleteAudio
                    )
                }
            }
        }
    }
}
