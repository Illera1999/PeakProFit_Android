package com.illera.peakprofit.feature.main.exercises.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.illera.peakprofit.R
import com.illera.peakprofit.domain.entity.Exercise

@Composable
fun ExerciseCard(
    exercise: Exercise,
    canSave: Boolean,
    isSaved: Boolean,
    onOpenDetail: (String) -> Unit,
    onSaveClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacingMedium = dimensionResource(R.dimen.spacing_medium)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onOpenDetail(exercise.id) }
    ) {
        Column(modifier = Modifier.padding(spacingMedium)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = exercise.name, fontWeight = FontWeight.SemiBold)
                if (canSave) {
                    IconButton(
                        onClick = { onSaveClick(exercise.id) }
                    ) {
                        Icon(
                            imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = if (isSaved) {
                                stringResource(R.string.exercise_unsave)
                            } else {
                                stringResource(R.string.exercise_save)
                            }
                        )
                    }
                }
            }
            Text(text = stringResource(R.string.exercise_body_part, exercise.bodyParts.joinToString()))
            Text(text = stringResource(R.string.exercise_target_muscles, exercise.targetMuscles.joinToString()))
            Text(text = stringResource(R.string.exercise_equipment, exercise.equipments.joinToString()))
        }
    }
}
