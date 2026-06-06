package com.illera.peakprofit.feature.main.saved_exercises

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.illera.peakprofit.R
import com.illera.peakprofit.core.ui.ScreenTopBar
import com.illera.peakprofit.core.ui.asString
import com.illera.peakprofit.feature.main.exercises.components.ExerciseCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedExercisesScreen(
    onBack: () -> Unit,
    onOpenExerciseDetail: (String) -> Unit,
    viewModel: SavedExercisesViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val errorMessage = state.errorMessage
    val spacingLarge = dimensionResource(R.dimen.spacing_large)
    val spacingMedium = dimensionResource(R.dimen.spacing_medium)
    val spacingSmall = dimensionResource(R.dimen.spacing_small)
    val spacingXLarge = dimensionResource(R.dimen.spacing_xlarge)

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
                .padding(paddingValues)
                .padding(spacingLarge),
            verticalArrangement = Arrangement.spacedBy(spacingMedium)
        ) {
            when {
                !state.isAuthenticated -> {
                    Text(text = stringResource(R.string.saved_exercises_sign_in_required))
                    Button(onClick = onBack) {
                        Text(stringResource(R.string.common_back))
                    }
                }
                errorMessage != null -> {
                    Text(text = errorMessage.asString())
                }
                state.items.isEmpty() -> {
                    Text(text = stringResource(R.string.saved_exercises_empty))
                }
                else -> {
                    Text(text = stringResource(R.string.saved_exercises_count, state.items.size))
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(spacingSmall),
                        contentPadding = PaddingValues(bottom = spacingXLarge)
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
                                onSaveClick = viewModel::onSaveClicked
                            )
                        }
                    }
                }
            }
        }
    }
}
