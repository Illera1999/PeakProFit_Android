package com.illera.peakprofit.feature.main.exercises

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.illera.peakprofit.R
import com.illera.peakprofit.core.theme.PeakTheme
import com.illera.peakprofit.core.theme.body
import com.illera.peakprofit.core.ui.asString
import com.illera.peakprofit.core.ui.components.PeakSearchBar
import com.illera.peakprofit.core.ui.components.PeakScreenTitle
import com.illera.peakprofit.core.ui.components.PeakSectionCard
import com.illera.peakprofit.core.ui.components.PeakTextButton
import com.illera.peakprofit.feature.main.exercises.components.ExerciseCard

@Composable
fun ExercisesScreen(
    onOpenExerciseDetail: (String) -> Unit,
    viewModel: ExercisesViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val spacing = PeakTheme.spacing

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PeakTheme.colors.canvas)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = spacing.large, vertical = spacing.large),
            contentAlignment = Alignment.Center
        ) {
            PeakScreenTitle(text = stringResource(R.string.exercises_title))
        }
        HorizontalDivider(color = PeakTheme.colors.divider)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = spacing.large, vertical = spacing.large),
            verticalArrangement = Arrangement.spacedBy(spacing.medium)
        ) {
            PeakSearchBar(
                value = state.query,
                onValueChange = viewModel::onQueryChanged,
                placeholder = stringResource(R.string.exercises_search_label),
                enabled = !state.isLoading && !state.isLoadingMore
            )

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                state.errorMessage != null -> {
                    PeakSectionCard {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(spacing.small)
                        ) {
                            Text(
                                text = state.errorMessage?.asString().orEmpty(),
                                style = MaterialTheme.typography.body,
                                color = PeakTheme.colors.danger
                            )
                            PeakTextButton(
                                text = stringResource(R.string.common_retry),
                                onClick = viewModel::retry
                            )
                        }
                    }
                }
                state.filteredItems.isEmpty() -> {
                    PeakSectionCard {
                        Text(
                            text = if (state.query.isBlank()) {
                                stringResource(R.string.exercises_empty_state)
                            } else {
                                stringResource(R.string.exercises_empty_results)
                            },
                            style = MaterialTheme.typography.body,
                            color = PeakTheme.colors.textSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                else -> {
                    Text(
                        text = stringResource(R.string.exercises_results, state.filteredItems.size),
                        style = MaterialTheme.typography.body,
                        color = PeakTheme.colors.textSecondary
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(spacing.medium),
                        contentPadding = PaddingValues(bottom = spacing.xxLarge)
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
                                LaunchedEffect(
                                    index,
                                    state.isLoadingMore,
                                    state.hasMore,
                                    state.query
                                ) {
                                    if (!state.isLoadingMore && state.hasMore) {
                                        viewModel.loadMore()
                                    }
                                }
                            }
                        }

                        if (state.isLoadingMore) {
                            item(key = "loading_more") {
                                PeakSectionCard {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(spacing.small)
                                    ) {
                                        CircularProgressIndicator()
                                        Text(
                                            text = stringResource(R.string.exercises_loading_more),
                                            style = MaterialTheme.typography.body,
                                            color = PeakTheme.colors.textSecondary
                                        )
                                    }
                                }
                            }
                        }

                        if (!state.hasMore && state.items.isNotEmpty() && state.query.isBlank()) {
                            item(key = "end_of_list") {
                                PeakSectionCard {
                                    Text(
                                        text = stringResource(R.string.exercises_end_of_list),
                                        style = MaterialTheme.typography.body,
                                        color = PeakTheme.colors.textSecondary
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
