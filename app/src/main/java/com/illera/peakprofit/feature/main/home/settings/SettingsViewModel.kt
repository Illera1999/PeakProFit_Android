package com.illera.peakprofit.feature.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private val mutableUiState = MutableStateFlow(
        SettingsUiState(
            languageTag = resolveCurrentLanguageTag()
        )
    )
    val uiState: StateFlow<SettingsUiState> = mutableUiState.asStateFlow()

    fun onLanguageChanged(languageTag: String) {
        val normalizedTag = when (languageTag) {
            "en", "es" -> languageTag
            else -> "es"
        }

        viewModelScope.launch {
            // AppCompat aplica el locale a nivel de aplicación y recrea las activities
            // activas para que Compose vuelva a resolver todos los string resources.
            AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags(normalizedTag)
            )
            mutableUiState.update { it.copy(languageTag = normalizedTag) }
        }
    }

    private fun resolveCurrentLanguageTag(): String {
        val appLocales = AppCompatDelegate.getApplicationLocales()
        val currentTag = appLocales.toLanguageTags()
            .substringBefore(',')
            .ifBlank { java.util.Locale.getDefault().language }

        // Reducimos variantes como `en-GB` o `es-ES` a los idiomas soportados en UI.
        return if (currentTag.startsWith("en")) "en" else "es"
    }
}
