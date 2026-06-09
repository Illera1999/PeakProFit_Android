package com.illera.peakprofit.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.illera.peakprofit.core.theme.PeakTheme

@Composable
fun PeakSectionCard(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(PeakTheme.spacing.large),
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = PeakTheme.shapes.card,
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = PeakTheme.elevations.card
    ) {
        Box(
            modifier = Modifier
                .then(
                    if (onClick != null) {
                        Modifier.clickable(onClick = onClick)
                    } else {
                        Modifier
                    }
                )
                .padding(contentPadding)
        ) {
            androidx.compose.foundation.layout.Column(content = content)
        }
    }
}
