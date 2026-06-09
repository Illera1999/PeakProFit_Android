package com.illera.peakprofit.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import com.illera.peakprofit.R
import com.illera.peakprofit.core.theme.PeakTheme

@Composable
fun PeakDetailHero(
    title: String,
    imageBitmap: ImageBitmap?,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.35f),
        shape = PeakTheme.shapes.card,
        color = MaterialTheme.colorScheme.surfaceVariant,
        shadowElevation = PeakTheme.elevations.card
    ) {
        if (imageBitmap != null) {
            Image(
                bitmap = imageBitmap,
                contentDescription = stringResource(R.string.exercise_image_description, title),
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.FitnessCenter,
                    contentDescription = null,
                    tint = PeakTheme.colors.textSecondary
                )
            }
        }
    }
}
