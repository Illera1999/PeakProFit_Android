package com.illera.peakprofit.feature.auth.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.illera.peakprofit.R
import com.illera.peakprofit.core.ui.UiText
import com.illera.peakprofit.core.ui.asString

@Composable
fun AuthForm(
    title: String,
    email: String,
    password: String,
    primaryActionText: String,
    secondaryActionText: String,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onPrimaryAction: () -> Unit,
    onSecondaryAction: () -> Unit,
    modifier: Modifier = Modifier,
    tertiaryActionText: String? = null,
    onTertiaryAction: (() -> Unit)? = null,
    errorMessage: UiText? = null,
    enabled: Boolean = true
) {
    val spacingLarge = dimensionResource(R.dimen.spacing_large)
    val spacingMedium = dimensionResource(R.dimen.spacing_medium)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(spacingLarge),
        verticalArrangement = Arrangement.spacedBy(spacingMedium)
    ) {
        Text(text = title)
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChanged,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.auth_email_label)) },
            enabled = enabled
        )
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChanged,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.auth_password_label)) },
            enabled = enabled
        )
        Button(
            onClick = onPrimaryAction,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled
        ) {
            Text(text = primaryActionText)
        }
        Button(
            onClick = onSecondaryAction,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled
        ) {
            Text(text = secondaryActionText)
        }
        if (tertiaryActionText != null && onTertiaryAction != null) {
            Button(
                onClick = onTertiaryAction,
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled
            ) {
                Text(text = tertiaryActionText)
            }
        }
        errorMessage?.let { message ->
            Text(text = message.asString())
        }
    }
}
