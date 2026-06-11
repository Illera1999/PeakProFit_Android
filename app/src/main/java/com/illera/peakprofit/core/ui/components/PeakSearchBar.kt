package com.illera.peakprofit.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.illera.peakprofit.R
import com.illera.peakprofit.core.theme.PeakTheme
import com.illera.peakprofit.core.theme.body

@Composable
fun PeakSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val borderWidth = dimensionResource(R.dimen.peak_stroke_border)
    val actionIconSize = dimensionResource(R.dimen.peak_size_icon_medium)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = PeakTheme.sizes.fieldMinHeight),
        shape = PeakTheme.shapes.field,
        color = if (enabled) PeakTheme.colors.inputBackground else PeakTheme.colors.disabledContainer,
        border = BorderStroke(borderWidth, PeakTheme.colors.inputBorder),
        shadowElevation = PeakTheme.elevations.field
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = PeakTheme.spacing.medium,
                vertical = PeakTheme.spacing.small
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(PeakTheme.spacing.small)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = PeakTheme.colors.textSecondary
            )
            Box(modifier = Modifier.weight(1f)) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.body,
                        color = PeakTheme.colors.textSecondary
                    )
                }
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = enabled,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.body.copy(
                        color = if (enabled) PeakTheme.colors.textPrimary else PeakTheme.colors.disabledContent
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
                )
            }
            if (value.isNotBlank()) {
                IconButton(
                    onClick = { onValueChange("") },
                    modifier = Modifier.size(actionIconSize),
                    enabled = enabled
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.common_clear_text),
                        tint = PeakTheme.colors.textSecondary
                    )
                }
            }
        }
    }
}
