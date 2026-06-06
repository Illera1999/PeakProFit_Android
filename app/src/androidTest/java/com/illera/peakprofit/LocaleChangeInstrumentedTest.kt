package com.illera.peakprofit

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.core.os.LocaleListCompat
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocaleChangeInstrumentedTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @After
    fun tearDown() {
        composeRule.activity.runOnUiThread {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList())
        }
    }

    @Test
    fun changingLocaleToEnglishUpdatesActivityResources() {
        composeRule.activity.runOnUiThread {
            AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags("en")
            )
        }

        composeRule.waitForIdle()

        assertEquals(
            "Settings",
            composeRule.activity.getString(R.string.settings_title)
        )
    }

    @Test
    fun changingLocaleToSpanishUpdatesActivityResources() {
        composeRule.activity.runOnUiThread {
            AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags("es")
            )
        }

        composeRule.waitForIdle()

        assertEquals(
            "Ajustes",
            composeRule.activity.getString(R.string.settings_title)
        )
    }
}
