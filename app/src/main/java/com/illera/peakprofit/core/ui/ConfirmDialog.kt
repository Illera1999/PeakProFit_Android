package com.illera.peakprofit.core.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.illera.peakprofit.core.theme.body
import com.illera.peakprofit.core.theme.screenTitle
import com.illera.peakprofit.core.ui.components.PeakTextButton

@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    confirmText: String,
    dismissText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.screenTitle
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.body
            )
        },
        confirmButton = {
            PeakTextButton(text = confirmText, onClick = onConfirm)
        },
        dismissButton = {
            PeakTextButton(text = dismissText, onClick = onDismiss)
        }
    )
}
