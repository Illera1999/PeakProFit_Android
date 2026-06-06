package com.illera.peakprofit.feature.main.exercise_detail

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.illera.peakprofit.R
import com.illera.peakprofit.core.ui.ScreenTopBar
import com.illera.peakprofit.core.ui.asString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailScreen(
    exerciseId: String,
    onBack: () -> Unit,
    viewModel: ExerciseDetailViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val errorMessage = state.errorMessage
    val exercise = state.exercise
    val spacingLarge = dimensionResource(R.dimen.spacing_large)
    val spacingMedium = dimensionResource(R.dimen.spacing_medium)
    val spacingSmall = dimensionResource(R.dimen.spacing_small)
    val spacingXLarge = dimensionResource(R.dimen.spacing_xlarge)

    LaunchedEffect(exerciseId) {
        viewModel.load(exerciseId)
    }

    Scaffold(
        topBar = {
            ScreenTopBar(
                title = stringResource(R.string.exercise_detail_title),
                onBack = onBack,
                actions = {
                    if (state.canSaveExercise) {
                        IconButton(onClick = viewModel::onSaveClicked) {
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
                .padding(paddingValues)
                .padding(spacingLarge),
            verticalArrangement = Arrangement.spacedBy(spacingMedium)
        ) {
            when {
                state.isLoading -> CircularProgressIndicator()
                errorMessage != null -> {
                    Text(text = errorMessage.asString())
                    Button(onClick = { viewModel.load(exerciseId) }) {
                        Text(stringResource(R.string.common_retry))
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
                        verticalArrangement = Arrangement.spacedBy(spacingSmall),
                        contentPadding = PaddingValues(bottom = spacingXLarge)
                    ) {
                        if (imageBitmap != null) {
                            item {
                                Image(
                                    bitmap = imageBitmap,
                                    contentDescription = stringResource(R.string.exercise_image_description, exercise.name),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1.6f),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                        item {
                            Text(text = exercise.name, fontWeight = FontWeight.Bold)
                            Text(text = stringResource(R.string.exercise_id, exercise.id))
                        }
                        item {
                            Text(text = stringResource(R.string.exercise_body_part, exercise.bodyParts.joinToString()))
                            Text(text = stringResource(R.string.exercise_target_muscles, exercise.targetMuscles.joinToString()))
                            Text(text = stringResource(R.string.exercise_equipment, exercise.equipments.joinToString()))
                            if (exercise.secondaryMuscles.isNotEmpty()) {
                                Text(text = stringResource(R.string.exercise_secondary_muscles, exercise.secondaryMuscles.joinToString()))
                            }
                        }
                        if (exercise.description.isNotBlank()) {
                            item {
                                Text(text = stringResource(R.string.exercise_description), fontWeight = FontWeight.SemiBold)
                                Text(text = exercise.description)
                            }
                        }
                        if (exercise.difficulty.isNotBlank() || exercise.category.isNotBlank()) {
                            item {
                                if (exercise.difficulty.isNotBlank()) {
                                    Text(text = stringResource(R.string.exercise_difficulty, exercise.difficulty))
                                }
                                if (exercise.category.isNotBlank()) {
                                    Text(text = stringResource(R.string.exercise_category, exercise.category))
                                }
                            }
                        }
                        if (exercise.instructions.isNotEmpty()) {
                            item {
                                Text(text = stringResource(R.string.exercise_instructions), fontWeight = FontWeight.SemiBold)
                            }
                            itemsIndexed(exercise.instructions) { index, instruction ->
                                Text(text = stringResource(R.string.exercise_instruction_step, index + 1, instruction))
                            }
                        }
                    }
                }
            }
        }
    }
}
