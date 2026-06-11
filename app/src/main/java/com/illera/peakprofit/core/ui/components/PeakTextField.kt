package com.illera.peakprofit.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.illera.peakprofit.R
import com.illera.peakprofit.core.theme.PeakTheme
import com.illera.peakprofit.core.theme.body
import com.illera.peakprofit.core.theme.bodyMuted
import com.illera.peakprofit.core.theme.sectionLabel

data class PeakTextFieldAction(
    val icon: ImageVector = Icons.Default.Close,
    val contentDescription: String,
    val onClick: () -> Unit
)

@Composable
fun PeakTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    isPassword: Boolean = false,
    errorText: String? = null,
    trailingAction: PeakTextFieldAction? = null,
    enabled: Boolean = true
) {
    val colors = PeakTheme.colors
    val borderWidth = dimensionResource(R.dimen.peak_stroke_border)
    val actionIconSize = dimensionResource(R.dimen.peak_size_icon_medium)
    var isPasswordVisible by remember(isPassword) { mutableStateOf(false) }
    val togglePasswordDescription = if (isPasswordVisible) {
        stringResource(R.string.auth_hide_password)
    } else {
        stringResource(R.string.auth_show_password)
    }
    val borderColor = when {
        errorText != null -> colors.danger
        else -> colors.inputBorder
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(PeakTheme.spacing.xSmall)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.sectionLabel,
            color = colors.textPrimary
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = PeakTheme.sizes.fieldMinHeight),
            shape = PeakTheme.shapes.field,
            color = if (enabled) colors.inputBackground else colors.disabledContainer,
            border = BorderStroke(borderWidth, borderColor),
            shadowElevation = PeakTheme.elevations.field
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = PeakTheme.spacing.medium, vertical = PeakTheme.spacing.small),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(PeakTheme.spacing.xSmall)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty() && placeholder != null) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.body,
                            color = colors.textSecondary
                        )
                    }

                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = enabled,
                        singleLine = true,
                        textStyle = MaterialTheme.typography.body.copy(
                            color = if (enabled) colors.textPrimary else colors.disabledContent
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Email
                        ),
                        visualTransformation = if (isPassword && !isPasswordVisible) {
                            PasswordVisualTransformation()
                        } else {
                            VisualTransformation.None
                        },
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
                    )
                }

                when {
                    isPassword -> {
                        IconButton(
                            onClick = { isPasswordVisible = !isPasswordVisible },
                            enabled = enabled,
                            modifier = Modifier.size(actionIconSize)
                        ) {
                            Icon(
                                imageVector = if (isPasswordVisible) {
                                    Icons.Default.VisibilityOff
                                } else {
                                    Icons.Default.Visibility
                                },
                                contentDescription = togglePasswordDescription,
                                tint = if (enabled) colors.textPrimary else colors.disabledContent
                            )
                        }
                    }
                    trailingAction != null -> {
                        IconButton(
                            onClick = trailingAction.onClick,
                            enabled = enabled,
                            modifier = Modifier.size(actionIconSize)
                        ) {
                            Icon(
                                imageVector = trailingAction.icon,
                                contentDescription = trailingAction.contentDescription,
                                tint = if (enabled) colors.textPrimary else colors.disabledContent
                            )
                        }
                    }
                }
            }
        }

        errorText?.let { message ->
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMuted,
                color = colors.danger
            )
        }
    }
}
