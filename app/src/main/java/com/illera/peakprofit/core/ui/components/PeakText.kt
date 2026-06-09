package com.illera.peakprofit.core.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.illera.peakprofit.core.theme.PeakTheme
import com.illera.peakprofit.core.theme.body
import com.illera.peakprofit.core.theme.bodyMuted
import com.illera.peakprofit.core.theme.screenTitle

@Composable
fun PeakScreenTitle(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.screenTitle,
        color = PeakTheme.colors.textPrimary,
        textAlign = textAlign
    )
}

@Composable
fun PeakBodyText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.body,
        color = PeakTheme.colors.textPrimary,
        textAlign = textAlign
    )
}

@Composable
fun PeakMutedText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodyMuted,
        color = PeakTheme.colors.textSecondary,
        textAlign = textAlign
    )
}
