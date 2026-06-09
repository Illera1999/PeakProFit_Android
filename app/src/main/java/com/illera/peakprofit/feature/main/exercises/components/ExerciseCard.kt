package com.illera.peakprofit.feature.main.exercises.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.illera.peakprofit.R
import com.illera.peakprofit.core.ui.components.PeakExerciseCard
import com.illera.peakprofit.domain.entity.Exercise

@Composable
fun ExerciseCard(
    exercise: Exercise,
    canSave: Boolean,
    isSaved: Boolean,
    onOpenDetail: (String) -> Unit,
    onSaveClick: (String) -> Unit,
    extraContent: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    PeakExerciseCard(
        title = exercise.name,
        subtitle = buildList {
            if (exercise.bodyParts.isNotEmpty()) add(exercise.bodyParts.joinToString())
            if (exercise.targetMuscles.isNotEmpty()) add(exercise.targetMuscles.joinToString())
        }.joinToString(" · "),
        metaChips = buildList {
            if (exercise.difficulty.isNotBlank()) add(exercise.difficulty)
            if (exercise.category.isNotBlank()) add(exercise.category)
            if (isEmpty() && exercise.equipments.isNotEmpty()) add(exercise.equipments.first())
        },
        modifier = modifier,
        isBookmarked = isSaved,
        canToggleBookmark = canSave,
        bookmarkContentDescription = if (isSaved) {
            stringResource(R.string.exercise_unsave)
        } else {
            stringResource(R.string.exercise_save)
        },
        onBookmarkClick = { onSaveClick(exercise.id) },
        onClick = { onOpenDetail(exercise.id) },
        extraContent = extraContent
    )
}
