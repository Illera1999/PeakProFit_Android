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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

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

    LaunchedEffect(exerciseId) {
        viewModel.load(exerciseId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de ejercicio") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Volver")
                    }
                },
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
                                    "Quitar ejercicio guardado"
                                } else {
                                    "Guardar ejercicio"
                                }
                            )
                        }
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
                state.isLoading -> CircularProgressIndicator()
                errorMessage != null -> {
                    Text(text = errorMessage)
                    Button(onClick = { viewModel.load(exerciseId) }) {
                        Text("Reintentar")
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
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 20.dp)
                    ) {
                        if (imageBitmap != null) {
                            item {
                                Image(
                                    bitmap = imageBitmap,
                                    contentDescription = "Imagen del ejercicio ${exercise.name}",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1.6f),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                        item {
                            Text(text = exercise.name, fontWeight = FontWeight.Bold)
                            Text(text = "ID: ${exercise.id}")
                        }
                        item {
                            Text(text = "Zona: ${exercise.bodyParts.joinToString()}")
                            Text(text = "Músculo objetivo: ${exercise.targetMuscles.joinToString()}")
                            Text(text = "Equipo: ${exercise.equipments.joinToString()}")
                            if (exercise.secondaryMuscles.isNotEmpty()) {
                                Text(text = "Músculos secundarios: ${exercise.secondaryMuscles.joinToString()}")
                            }
                        }
                        if (exercise.description.isNotBlank()) {
                            item {
                                Text(text = "Descripción", fontWeight = FontWeight.SemiBold)
                                Text(text = exercise.description)
                            }
                        }
                        if (exercise.difficulty.isNotBlank() || exercise.category.isNotBlank()) {
                            item {
                                if (exercise.difficulty.isNotBlank()) Text(text = "Dificultad: ${exercise.difficulty}")
                                if (exercise.category.isNotBlank()) Text(text = "Categoría: ${exercise.category}")
                            }
                        }
                        if (exercise.instructions.isNotEmpty()) {
                            item {
                                Text(text = "Instrucciones", fontWeight = FontWeight.SemiBold)
                            }
                            itemsIndexed(exercise.instructions) { index, instruction ->
                                Text(text = "${index + 1}. $instruction")
                            }
                        }
                    }
                }
            }
        }
    }
}
