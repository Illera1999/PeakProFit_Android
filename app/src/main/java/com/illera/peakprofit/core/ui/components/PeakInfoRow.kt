package com.illera.peakprofit.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.illera.peakprofit.core.theme.PeakTheme
import com.illera.peakprofit.core.theme.body
import com.illera.peakprofit.core.theme.bodyMuted

@Composable
fun PeakInfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(PeakTheme.spacing.small)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMuted,
            color = PeakTheme.colors.textSecondary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.body,
            color = PeakTheme.colors.textPrimary
        )
    }
}
