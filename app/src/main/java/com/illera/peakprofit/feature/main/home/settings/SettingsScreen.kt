package com.illera.peakprofit.feature.main.home.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.illera.peakprofit.R
import com.illera.peakprofit.core.ui.ScreenTopBar

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val spacingLarge = dimensionResource(R.dimen.spacing_large)
    val spacingMedium = dimensionResource(R.dimen.spacing_medium)
    val options = listOf(
        LanguageOption(
            tag = "es",
            label = stringResource(R.string.settings_language_spanish)
        ),
        LanguageOption(
            tag = "en",
            label = stringResource(R.string.settings_language_english)
        )
    )

    Column(modifier = Modifier.fillMaxSize()) {
        ScreenTopBar(
            title = stringResource(R.string.settings_title),
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(spacingLarge),
            verticalArrangement = Arrangement.spacedBy(spacingLarge)
        ) {
            Text(
                text = stringResource(R.string.settings_title),
                style = MaterialTheme.typography.headlineMedium
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(spacingLarge),
                    verticalArrangement = Arrangement.spacedBy(spacingMedium)
                ) {
                    Text(
                        text = stringResource(R.string.settings_language),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = stringResource(R.string.settings_language_description),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    options.forEach { option ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = uiState.languageTag == option.tag,
                                onClick = { viewModel.onLanguageChanged(option.tag) }
                            )
                            Text(text = option.label)
                        }
                    }
                }
            }
        }
    }
}

private data class LanguageOption(
    val tag: String,
    val label: String
)
