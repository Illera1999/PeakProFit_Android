package com.illera.peakprofit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.illera.peakprofit.core.navigation.PeakProFitNavHost
import com.illera.peakprofit.core.theme.PeakProFitTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PeakProFitTheme {
                PeakProFitRoot()
            }
        }
    }
}

@Composable
private fun PeakProFitRoot() {
    PeakProFitNavHost()
}
