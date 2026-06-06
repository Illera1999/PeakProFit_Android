package com.illera.peakprofit.feature.main.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import com.illera.peakprofit.R

@Composable
fun InfoCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val spacingMedium = dimensionResource(R.dimen.spacing_medium)

    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(spacingMedium)) {
            Text(text = title, fontWeight = FontWeight.SemiBold)
            Text(text = value)
        }
    }
}
