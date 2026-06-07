package com.illera.peakprofit.feature.main.exercises

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.illera.peakprofit.R
import com.illera.peakprofit.core.ui.asString
import com.illera.peakprofit.feature.main.exercises.components.ExerciseCard

@Composable
fun ExercisesScreen(
    onOpenExerciseDetail: (String) -> Unit,
    viewModel: ExercisesViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val spacingLarge = dimensionResource(R.dimen.spacing_large)
    val spacingMedium = dimensionResource(R.dimen.spacing_medium)
    val spacingSmall = dimensionResource(R.dimen.spacing_small)
    val spacingXSmall = dimensionResource(R.dimen.spacing_xsmall)
    val spacingXLarge = dimensionResource(R.dimen.spacing_xlarge)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacingLarge),
        verticalArrangement = Arrangement.spacedBy(spacingMedium)
    ) {
        Text(text = stringResource(R.string.exercises_title))
        OutlinedTextField(
            value = state.query,
            onValueChange = viewModel::onQueryChanged,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.exercises_search_label)) },
            enabled = !state.isLoading && !state.isLoadingMore
        )

        if (state.isLoading) {
            CircularProgressIndicator()
        } else if (state.errorMessage != null) {
            Text(text = state.errorMessage?.asString().orEmpty())
            Button(onClick = viewModel::retry) {
                Text(stringResource(R.string.common_retry))
            }
        } else {
            Text(text = stringResource(R.string.exercises_results, state.filteredItems.size))
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(spacingSmall),
                contentPadding = PaddingValues(bottom = spacingXLarge)
            ) {
                itemsIndexed(
                    items = state.filteredItems,
                    key = { index, exercise -> "${exercise.id}-$index" }
                ) { index, exercise ->
                    ExerciseCard(
                        exercise = exercise,
                        canSave = state.canSaveExercises,
                        isSaved = state.savedExerciseIds.contains(exercise.id),
                        onOpenDetail = onOpenExerciseDetail,
                        onSaveClick = viewModel::onSaveClicked
                    )

                    if (index >= state.filteredItems.lastIndex - 2 && state.query.isBlank()) {
                        LaunchedEffect(index, state.isLoadingMore, state.hasMore, state.query) {
                            if (!state.isLoadingMore && state.hasMore) {
                                viewModel.loadMore()
                            }
                        }
                    }
                }

                if (state.isLoadingMore) {
                    item(key = "loading_more") {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(spacingMedium),
                            verticalArrangement = Arrangement.spacedBy(spacingXSmall)
                        ) {
                            CircularProgressIndicator()
                            Text(text = stringResource(R.string.exercises_loading_more))
                        }
                    }
                }

                if (!state.hasMore && state.items.isNotEmpty() && state.query.isBlank()) {
                    item(key = "end_of_list") {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = stringResource(R.string.exercises_end_of_list),
                                modifier = Modifier.padding(spacingMedium)
                            )
                        }
                    }
                }
            }
        }
    }
}
