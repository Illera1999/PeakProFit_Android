package com.illera.peakprofit.feature.main.exercise_detail

import android.graphics.BitmapFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.illera.peakprofit.R
import com.illera.peakprofit.core.theme.PeakTheme
import com.illera.peakprofit.core.theme.body
import com.illera.peakprofit.core.ui.ConfirmDialog
import com.illera.peakprofit.core.ui.ScreenTopBar
import com.illera.peakprofit.core.ui.asString
import com.illera.peakprofit.core.ui.components.PeakBodyText
import com.illera.peakprofit.core.ui.components.PeakChipRow
import com.illera.peakprofit.core.ui.components.PeakDetailHero
import com.illera.peakprofit.core.ui.components.PeakInfoRow
import com.illera.peakprofit.core.ui.components.PeakPrimaryButton
import com.illera.peakprofit.core.ui.components.PeakScreenTitle
import com.illera.peakprofit.core.ui.components.PeakSectionCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailScreen(
    exerciseId: String,
    onBack: () -> Unit,
    viewModel: ExerciseDetailViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showRemoveConfirmation by remember { mutableStateOf(false) }
    val errorMessage = state.errorMessage
    val exercise = state.exercise
    val spacing = PeakTheme.spacing

    LaunchedEffect(exerciseId) {
        viewModel.load(exerciseId)
    }

    if (showRemoveConfirmation) {
        ConfirmDialog(
            title = stringResource(R.string.saved_exercises_remove_title),
            message = stringResource(
                R.string.saved_exercises_remove_message,
                exercise?.name.orEmpty()
            ),
            confirmText = stringResource(R.string.saved_exercises_remove_confirm),
            dismissText = stringResource(R.string.exit_dialog_dismiss),
            onConfirm = {
                showRemoveConfirmation = false
                viewModel.onSaveClicked()
            },
            onDismiss = { showRemoveConfirmation = false }
        )
    }

    Scaffold(
        topBar = {
            ScreenTopBar(
                title = stringResource(R.string.exercise_detail_title),
                onBack = onBack,
                actions = {
                    if (state.canSaveExercise) {
                        IconButton(
                            onClick = {
                                if (state.isExerciseSaved) {
                                    showRemoveConfirmation = true
                                } else {
                                    viewModel.onSaveClicked()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (state.isExerciseSaved) {
                                    Icons.Filled.Bookmark
                                } else {
                                    Icons.Outlined.BookmarkBorder
                                },
                                contentDescription = if (state.isExerciseSaved) {
                                    stringResource(R.string.exercise_unsave)
                                } else {
                                    stringResource(R.string.exercise_save)
                                }
                            )
                        }
                    }
                }
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
                state.isLoading -> CircularProgressIndicator()
                errorMessage != null -> {
                    PeakSectionCard {
                        Column(verticalArrangement = Arrangement.spacedBy(spacing.small)) {
                            Text(
                                text = errorMessage.asString(),
                                style = MaterialTheme.typography.body,
                                color = PeakTheme.colors.danger
                            )
                            PeakPrimaryButton(
                                text = stringResource(R.string.common_retry),
                                onClick = { viewModel.load(exerciseId) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
                exercise != null -> {
                    val imageBitmap = remember(state.imageData) {
                        state.imageData?.let { imageBytes ->
                            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)?.asImageBitmap()
                        }
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(spacing.medium),
                        contentPadding = PaddingValues(bottom = spacing.xxLarge)
                    ) {
                        item {
                            PeakDetailHero(
                                title = exercise.name,
                                imageBitmap = imageBitmap
                            )
                        }
                        item {
                            PeakSectionCard {
                                Column(verticalArrangement = Arrangement.spacedBy(spacing.small)) {
                                    PeakScreenTitle(text = exercise.name)
                                    PeakInfoRow(
                                        label = stringResource(R.string.exercise_id_label),
                                        value = exercise.id
                                    )
                                    PeakChipRow(
                                        items = buildList {
                                            addAll(exercise.bodyParts)
                                            addAll(exercise.targetMuscles)
                                            addAll(exercise.equipments.take(1))
                                            if (exercise.difficulty.isNotBlank()) add(exercise.difficulty)
                                            if (exercise.category.isNotBlank()) add(exercise.category)
                                        }
                                    )
                                }
                            }
                        }
                        item {
                            PeakSectionCard {
                                Column(verticalArrangement = Arrangement.spacedBy(spacing.small)) {
                                    PeakInfoRow(
                                        label = stringResource(R.string.exercise_body_part_label),
                                        value = exercise.bodyParts.joinToString()
                                    )
                                    PeakInfoRow(
                                        label = stringResource(R.string.exercise_target_muscles_label),
                                        value = exercise.targetMuscles.joinToString()
                                    )
                                    PeakInfoRow(
                                        label = stringResource(R.string.exercise_equipment_label),
                                        value = exercise.equipments.joinToString()
                                    )
                                    if (exercise.secondaryMuscles.isNotEmpty()) {
                                        PeakInfoRow(
                                            label = stringResource(R.string.exercise_secondary_muscles_label),
                                            value = exercise.secondaryMuscles.joinToString()
                                        )
                                    }
                                }
                            }
                        }
                        if (exercise.description.isNotBlank()) {
                            item {
                                PeakSectionCard {
                                    Column(verticalArrangement = Arrangement.spacedBy(spacing.small)) {
                                        Text(
                                            text = stringResource(R.string.exercise_description),
                                            style = MaterialTheme.typography.titleMedium,
                                            color = PeakTheme.colors.textPrimary
                                        )
                                        PeakBodyText(text = exercise.description)
                                    }
                                }
                            }
                        }
                        if (exercise.instructions.isNotEmpty()) {
                            item {
                                PeakSectionCard {
                                    Column(verticalArrangement = Arrangement.spacedBy(spacing.small)) {
                                        Text(
                                            text = stringResource(R.string.exercise_instructions),
                                            style = MaterialTheme.typography.titleMedium,
                                            color = PeakTheme.colors.textPrimary
                                        )
                                        exercise.instructions.forEachIndexed { index, instruction ->
                                            PeakBodyText(
                                                text = stringResource(
                                                    R.string.exercise_instruction_step,
                                                    index + 1,
                                                    instruction
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
