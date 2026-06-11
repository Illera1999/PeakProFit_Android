package com.illera.peakprofit.feature.main.tap

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.ui.graphics.Color
import com.illera.peakprofit.R
import com.illera.peakprofit.core.ui.ConfirmDialog
import com.illera.peakprofit.core.ui.components.PeakBottomNavigationBar
import com.illera.peakprofit.core.ui.components.PeakBottomNavigationItem
import com.illera.peakprofit.feature.main.exercises.ExercisesScreen
import com.illera.peakprofit.feature.main.profile.ProfileScreen

@Composable
fun MainTabsScreen(
    onLoggedOut: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onOpenExerciseDetail: (String) -> Unit,
    onOpenSavedExercises: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activity = LocalActivity.current
    var showExitDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val tabs = listOf(
        PeakBottomNavigationItem(
            label = stringResource(R.string.tab_exercises),
            selectedIcon = Icons.Filled.FitnessCenter,
            unselectedIcon = Icons.Outlined.FitnessCenter
        ),
        PeakBottomNavigationItem(
            label = stringResource(R.string.tab_profile),
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.PersonOutline
        )
    )
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { tabs.size })

    if (showExitDialog) {
        ConfirmDialog(
            title = stringResource(R.string.exit_dialog_title),
            message = stringResource(R.string.exit_dialog_message),
            confirmText = stringResource(R.string.exit_dialog_confirm),
            dismissText = stringResource(R.string.exit_dialog_dismiss),
            onConfirm = {
                showExitDialog = false
                activity?.finish()
            },
            onDismiss = { showExitDialog = false }
        )
    }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            PeakBottomNavigationBar(
                items = tabs,
                selectedIndex = pagerState.currentPage,
                onSelect = { index ->
                    if (pagerState.currentPage == index) return@PeakBottomNavigationBar
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) { page ->
            when (page) {
                0 -> ExercisesScreen(onOpenExerciseDetail = onOpenExerciseDetail)
                1 -> ProfileScreen(
                    onLoggedOut = onLoggedOut,
                    onNavigateToLogin = onNavigateToLogin,
                    onOpenSavedExercises = onOpenSavedExercises,
                    onOpenSettings = onOpenSettings
                )
            }
        }

        // Se declara después del Pager para priorizar este manejador sobre el de navegación
        // y garantizar que el botón atrás abra primero el diálogo de salida.
        BackHandler {
            showExitDialog = true
        }
    }
}
