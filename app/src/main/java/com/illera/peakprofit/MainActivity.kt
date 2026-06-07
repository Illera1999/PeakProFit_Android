package com.illera.peakprofit

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.illera.peakprofit.core.navigation.NavigationWrapper
import com.illera.peakprofit.core.theme.PeakProFitTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // AppCompatActivity es necesaria para que AppCompatDelegate pueda recrear la
        // actividad correctamente al cambiar el idioma desde la pantalla de ajustes.
        enableEdgeToEdge()
        setContent {
            PeakProFitTheme {
                NavigationWrapper()
            }
        }
    }
}
