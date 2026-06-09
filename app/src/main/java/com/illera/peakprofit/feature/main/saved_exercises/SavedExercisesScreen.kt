package com.illera.peakprofit.feature.main.saved_exercises

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.illera.peakprofit.R
import com.illera.peakprofit.core.theme.PeakTheme
import com.illera.peakprofit.core.theme.body
import com.illera.peakprofit.core.ui.ConfirmDialog
import com.illera.peakprofit.core.ui.ScreenTopBar
import com.illera.peakprofit.core.ui.asString
import com.illera.peakprofit.core.ui.components.PeakSectionCard
import com.illera.peakprofit.core.ui.components.PeakTextButton
import com.illera.peakprofit.domain.entity.Exercise
import com.illera.peakprofit.feature.main.exercises.components.ExerciseCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedExercisesScreen(
    onBack: () -> Unit,
    onOpenExerciseDetail: (String) -> Unit,
    viewModel: SavedExercisesViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var pendingRemoval by remember { mutableStateOf<Exercise?>(null) }
    val errorMessage = state.errorMessage
    val spacing = PeakTheme.spacing

    pendingRemoval?.let { exercise ->
        ConfirmDialog(
            title = stringResource(R.string.saved_exercises_remove_title),
            message = stringResource(R.string.saved_exercises_remove_message, exercise.name),
            confirmText = stringResource(R.string.saved_exercises_remove_confirm),
            dismissText = stringResource(R.string.exit_dialog_dismiss),
            onConfirm = {
                viewModel.onSaveClicked(exercise.id)
                pendingRemoval = null
            },
            onDismiss = { pendingRemoval = null }
        )
    }

    Scaffold(
        topBar = {
            ScreenTopBar(
                title = stringResource(R.string.saved_exercises_title),
                onBack = onBack
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PeakTheme.colors.canvas)
                .padding(paddingValues)
                .padding(spacing.large),
            verticalArrangement = Arrangement.spacedBy(spacing.medium)
        ) {
            when {
                !state.isAuthenticated -> {
                    PeakSectionCard {
                        Column(verticalArrangement = Arrangement.spacedBy(spacing.small)) {
                            Text(
                                text = stringResource(R.string.saved_exercises_sign_in_required),
                                style = MaterialTheme.typography.body,
                                color = PeakTheme.colors.textSecondary
                            )
                            PeakTextButton(
                                text = stringResource(R.string.common_back),
                                onClick = onBack
                            )
                        }
                    }
                }
                errorMessage != null -> {
                    PeakSectionCard {
                        Text(
                            text = errorMessage.asString(),
                            style = MaterialTheme.typography.body,
                            color = PeakTheme.colors.danger
                        )
                    }
                }
                state.items.isEmpty() -> {
                    PeakSectionCard {
                        Text(
                            text = stringResource(R.string.saved_exercises_empty),
                            style = MaterialTheme.typography.body,
                            color = PeakTheme.colors.textSecondary
                        )
                    }
                }
                else -> {
                    Text(
                        text = stringResource(R.string.saved_exercises_count, state.items.size),
                        style = MaterialTheme.typography.body,
                        color = PeakTheme.colors.textSecondary
                    )
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(spacing.medium),
                        contentPadding = PaddingValues(bottom = spacing.xxLarge)
                    ) {
                        items(
                            items = state.items,
                            key = { exercise -> exercise.id }
                        ) { exercise ->
                            ExerciseCard(
                                exercise = exercise,
                                canSave = true,
                                isSaved = true,
                                onOpenDetail = onOpenExerciseDetail,
                                onSaveClick = { pendingRemoval = exercise },
                                extraContent = {
                                    SavedExerciseNoteField(
                                        exercise = exercise,
                                        onNoteSaved = viewModel::onSavedNoteChanged,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = spacing.medium)
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SavedExerciseNoteField(
    exercise: Exercise,
    onNoteSaved: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var note by rememberSaveable(exercise.id) { mutableStateOf(exercise.savedNote) }
    var hadFocus by remember { mutableStateOf(false) }

    LaunchedEffect(exercise.id) {
        note = exercise.savedNote
    }

    OutlinedTextField(
        value = note,
        onValueChange = { note = it },
        modifier = modifier.onFocusChanged { focusState ->
            val isFocused = focusState.isFocused
            // Se persiste al perder foco para evitar que el valor reactivo del DataStore
            // recoloque el cursor en cada pulsación.
            if (hadFocus && !isFocused && note != exercise.savedNote) {
                onNoteSaved(exercise.id, note)
            }
            hadFocus = isFocused
        },
        label = {
            Text(stringResource(R.string.saved_exercises_note_label))
        },
        placeholder = {
            Text(stringResource(R.string.saved_exercises_note_placeholder))
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default),
        singleLine = false,
        minLines = 3,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = PeakTheme.colors.inputBorder,
            focusedLabelColor = PeakTheme.colors.textSecondary,
            unfocusedLabelColor = PeakTheme.colors.textSecondary,
            focusedTextColor = PeakTheme.colors.textPrimary,
            unfocusedTextColor = PeakTheme.colors.textPrimary
        )
    )
}
