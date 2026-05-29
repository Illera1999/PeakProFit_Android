package com.illera.peakprofit.feature.main.exercises

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ExercisesScreen(
    onOpenExerciseDetail: (String) -> Unit,
    viewModel: ExercisesViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "Ejercicios")
        OutlinedTextField(
            value = state.query,
            onValueChange = viewModel::onQueryChanged,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Buscar por nombre, músculo o equipo") },
            enabled = !state.isLoading && !state.isLoadingMore
        )

        if (state.isLoading) {
            CircularProgressIndicator()
        } else if (state.errorMessage != null) {
            Text(text = state.errorMessage ?: "")
            Button(onClick = viewModel::retry) {
                Text("Reintentar")
            }
        } else {
            Text(text = "Resultados: ${state.filteredItems.size}")
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                itemsIndexed(
                    items = state.filteredItems,
                    key = { index, exercise -> "${exercise.id}-$index" }
                ) { index, exercise ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenExerciseDetail(exercise.id) }
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = exercise.name, fontWeight = FontWeight.SemiBold)
                            Text(text = "Zona: ${exercise.bodyParts.joinToString()}")
                            Text(text = "Músculo objetivo: ${exercise.targetMuscles.joinToString()}")
                            Text(text = "Equipo: ${exercise.equipments.joinToString()}")
                        }
                    }

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
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CircularProgressIndicator()
                            Text(text = "Cargando más ejercicios...")
                        }
                    }
                }

                if (!state.hasMore && state.items.isNotEmpty() && state.query.isBlank()) {
                    item(key = "end_of_list") {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Has llegado al final de la lista.",
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
