package com.illera.peakprofit.feature.exercises

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ExercisesScreen(
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
            enabled = !state.isLoading
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
                ) { _, exercise ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(text = exercise.name)
                        Text(text = "Zona: ${exercise.bodyParts.joinToString()}")
                        Text(text = "Músculo objetivo: ${exercise.targetMuscles.joinToString()}")
                        Text(text = "Equipo: ${exercise.equipments.joinToString()}")
                    }
                }
            }
        }
    }
}
