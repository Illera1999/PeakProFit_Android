package com.illera.peakprofit.feature.main.tap

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import com.illera.peakprofit.core.ui.ConfirmDialog
import com.illera.peakprofit.feature.main.exercises.ExercisesScreen
import com.illera.peakprofit.feature.main.home.HomeScreen

@Composable
fun MainTabsScreen(
    onLoggedOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activity = LocalActivity.current
    var showExitDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val tabs = listOf(
        TabItem(label = "Ejercicios"),
        TabItem(label = "Perfil")
    )
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { tabs.size })

    if (showExitDialog) {
        ConfirmDialog(
            title = "Salir de la app",
            message = "¿Seguro que quieres salir?",
            confirmText = "Salir",
            dismissText = "Cancelar",
            onConfirm = {
                showExitDialog = false
                activity?.finish()
            },
            onDismiss = { showExitDialog = false }
        )
    }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            if (pagerState.currentPage == index) return@NavigationBarItem
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        label = { Text(text = tab.label) },
                        icon = {}
                    )
                }
            }
        }
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) { page ->
            when (page) {
                0 -> ExercisesScreen()
                1 -> HomeScreen(onLoggedOut = onLoggedOut)
            }
        }

        // Se declara después del Pager para priorizar este manejador sobre el de navegación
        // y garantizar que el botón atrás abra primero el diálogo de salida.
        BackHandler {
            showExitDialog = true
        }
    }
}

private data class TabItem(
    val label: String
)
