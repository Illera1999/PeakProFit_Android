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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ejercicios guardados") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors()
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            when {
                !state.isAuthenticated -> {
                    Text(text = "Necesitas iniciar sesión para ver los ejercicios guardados.")
                    Button(onClick = onBack) {
                        Text("Volver")
                    }
                }
                errorMessage != null -> {
                    Text(text = errorMessage)
                }
                state.items.isEmpty() -> {
                    Text(text = "No hay ejercicios guardados todavía.")
                }
                else -> {
                    Text(text = "Guardados: ${state.items.size}")
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 20.dp)
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
