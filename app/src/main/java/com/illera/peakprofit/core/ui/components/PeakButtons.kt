package com.illera.peakprofit.core.ui.components

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.illera.peakprofit.core.theme.PeakTheme
import com.illera.peakprofit.core.theme.body
import com.illera.peakprofit.core.theme.button
import com.illera.peakprofit.core.theme.link

enum class PeakTextButtonStyle {
    Link,
    Secondary
}

@Composable
fun PeakPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = PeakTheme.sizes.buttonMinHeight),
        enabled = enabled && !loading,
        shape = PeakTheme.shapes.button,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = PeakTheme.colors.disabledContainer,
            disabledContentColor = PeakTheme.colors.disabledContent
        )
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(18.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.button
            )
        }
    }
}

@Composable
fun PeakTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: PeakTextButtonStyle = PeakTextButtonStyle.Link
) {
    val textColor = when {
        !enabled -> PeakTheme.colors.disabledContent
        style == PeakTextButtonStyle.Link -> PeakTheme.colors.link
        else -> PeakTheme.colors.textSecondary
    }

    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(
            contentColor = textColor,
            disabledContentColor = PeakTheme.colors.disabledContent
        )
    ) {
        Text(
            text = text,
            style = if (style == PeakTextButtonStyle.Link) {
                MaterialTheme.typography.link
            } else {
                MaterialTheme.typography.body
            }
        )
    }
}
