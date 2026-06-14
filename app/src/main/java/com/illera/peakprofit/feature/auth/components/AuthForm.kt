package com.illera.peakprofit.feature.auth.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.illera.peakprofit.R
import com.illera.peakprofit.core.theme.PeakTheme
import com.illera.peakprofit.core.ui.UiText
import com.illera.peakprofit.core.ui.asString
import com.illera.peakprofit.core.ui.components.PeakPrimaryButton
import com.illera.peakprofit.core.ui.components.PeakScreenTitle
import com.illera.peakprofit.core.ui.components.PeakTextButton
import com.illera.peakprofit.core.ui.components.PeakTextButtonStyle
import com.illera.peakprofit.core.ui.components.PeakTextField
import com.illera.peakprofit.core.ui.components.PeakTextFieldAction
import com.illera.peakprofit.core.ui.components.PeakMutedText
import com.illera.peakprofit.core.theme.bodyMuted

@Composable
fun AuthForm(
    title: String,
    email: String,
    password: String,
    confirmPassword: String? = null,
    primaryActionText: String,
    secondaryActionText: String,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onConfirmPasswordChanged: ((String) -> Unit)? = null,
    onPrimaryAction: () -> Unit,
    onSecondaryAction: () -> Unit,
    modifier: Modifier = Modifier,
    tertiaryActionText: String? = null,
    onTertiaryAction: (() -> Unit)? = null,
    supportingText: String? = null,
    onSupportingTextClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    supportingTextEnabled: Boolean = enabled,
    successMessage: UiText? = null,
    errorMessage: UiText? = null,
    loading: Boolean = false
) {
    val colors = PeakTheme.colors
    val spacing = PeakTheme.spacing
    val clearDescription = stringResource(R.string.common_clear_text)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = spacing.large, vertical = spacing.large),
            contentAlignment = Alignment.Center
        ) {
            PeakScreenTitle(text = title)
        }

        HorizontalDivider(color = colors.divider)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.canvas)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = spacing.large, vertical = spacing.xxLarge),
            verticalArrangement = Arrangement.spacedBy(spacing.large)
        ) {
            PeakTextField(
                value = email,
                onValueChange = onEmailChanged,
                label = stringResource(R.string.auth_email_label),
                enabled = enabled,
                trailingAction = email.takeIf { it.isNotEmpty() }?.let {
                    PeakTextFieldAction(
                        contentDescription = clearDescription,
                        onClick = { onEmailChanged("") }
                    )
                }
            )

            Column(verticalArrangement = Arrangement.spacedBy(spacing.small)) {
                PeakTextField(
                    value = password,
                    onValueChange = onPasswordChanged,
                    label = stringResource(R.string.auth_password_label),
                    isPassword = true,
                    enabled = enabled
                )

                if (confirmPassword != null && onConfirmPasswordChanged != null) {
                    PeakTextField(
                        value = confirmPassword,
                        onValueChange = onConfirmPasswordChanged,
                        label = stringResource(R.string.auth_confirm_password_label),
                        isPassword = true,
                        enabled = enabled
                    )
                }

                supportingText?.let { text ->
                    if (onSupportingTextClick != null) {
                        PeakTextButton(
                            text = text,
                            onClick = onSupportingTextClick,
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            enabled = supportingTextEnabled,
                            style = PeakTextButtonStyle.Secondary
                        )
                    } else {
                        PeakMutedText(
                            text = text,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            successMessage?.let { message ->
                Text(
                    text = message.asString(),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMuted,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            }

            errorMessage?.let { message ->
                Text(
                    text = message.asString(),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMuted,
                    color = colors.danger,
                    textAlign = TextAlign.Center
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = spacing.xLarge),
                verticalArrangement = Arrangement.spacedBy(spacing.small)
            ) {
                PeakPrimaryButton(
                    text = primaryActionText,
                    onClick = onPrimaryAction,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = enabled,
                    loading = loading
                )

                if (tertiaryActionText != null && onTertiaryAction != null) {
                    PeakTextButton(
                        text = tertiaryActionText,
                        onClick = onTertiaryAction,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        enabled = enabled,
                        style = PeakTextButtonStyle.Secondary
                    )
                }

                PeakTextButton(
                    text = secondaryActionText,
                    onClick = onSecondaryAction,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    enabled = enabled,
                    style = PeakTextButtonStyle.Link
                )
            }
        }
    }
}
