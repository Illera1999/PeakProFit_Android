package com.illera.peakprofit.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import com.illera.peakprofit.R
import com.illera.peakprofit.core.theme.PeakTheme
import com.illera.peakprofit.core.theme.bodyMuted

@Composable
fun PeakFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderWidth = dimensionResource(R.dimen.peak_stroke_border)

    Surface(
        modifier = modifier,
        onClick = onClick,
        shape = PeakTheme.shapes.button,
        color = if (selected) {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)
        } else {
            PeakTheme.colors.inputBackground
        },
        contentColor = if (selected) MaterialTheme.colorScheme.primary else PeakTheme.colors.textSecondary,
        border = BorderStroke(
            width = borderWidth,
            color = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.35f) else PeakTheme.colors.inputBorder
        )
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(
                horizontal = PeakTheme.spacing.medium,
                vertical = PeakTheme.spacing.xSmall
            ),
            style = MaterialTheme.typography.bodyMuted
        )
    }
}

@Composable
fun PeakMetaChip(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = PeakTheme.colors.textSecondary
) {
    Surface(
        modifier = modifier,
        shape = PeakTheme.shapes.button,
        color = backgroundColor,
        contentColor = contentColor
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(
                horizontal = PeakTheme.spacing.medium,
                vertical = PeakTheme.spacing.xSmall
            ),
            style = MaterialTheme.typography.bodyMuted
        )
    }
}

@Composable
fun PeakChipRow(
    items: List<String>,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(PeakTheme.spacing.small),
        verticalArrangement = Arrangement.spacedBy(PeakTheme.spacing.small)
    ) {
        items.forEach { item ->
            PeakMetaChip(text = item)
        }
    }
}
