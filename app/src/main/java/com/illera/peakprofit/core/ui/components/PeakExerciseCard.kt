package com.illera.peakprofit.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.illera.peakprofit.core.theme.PeakTheme
import com.illera.peakprofit.core.theme.body

@Composable
fun PeakExerciseCard(
    title: String,
    subtitle: String,
    metaChips: List<String>,
    modifier: Modifier = Modifier,
    isBookmarked: Boolean = false,
    canToggleBookmark: Boolean = false,
    bookmarkContentDescription: String = "",
    onBookmarkClick: (() -> Unit)? = null,
    onClick: () -> Unit,
    extraContent: (@Composable () -> Unit)? = null
) {
    PeakSectionCard(
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(PeakTheme.spacing.small)
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(PeakTheme.spacing.small)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(PeakTheme.spacing.xxSmall)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = PeakTheme.colors.textPrimary
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.body,
                        color = PeakTheme.colors.textSecondary
                    )
                }
                if (canToggleBookmark && onBookmarkClick != null) {
                    IconButton(onClick = onBookmarkClick) {
                        Icon(
                            imageVector = if (isBookmarked) {
                                Icons.Filled.Bookmark
                            } else {
                                Icons.Outlined.BookmarkBorder
                            },
                            contentDescription = bookmarkContentDescription,
                            tint = if (isBookmarked) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                PeakTheme.colors.textSecondary
                            }
                        )
                    }
                }
            }
            PeakChipRow(items = metaChips.take(3))
            extraContent?.invoke()
        }
    }
}
