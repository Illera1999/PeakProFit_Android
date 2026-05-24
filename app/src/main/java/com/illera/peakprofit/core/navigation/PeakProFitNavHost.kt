package com.illera.peakprofit.core.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.illera.peakprofit.feature.home.HomeScreen
import com.illera.peakprofit.feature.login.LoginScreen
import com.illera.peakprofit.feature.profile.ProfileScreen
import com.illera.peakprofit.feature.progress.ProgressScreen
import com.illera.peakprofit.feature.splash.SplashScreen
import com.illera.peakprofit.feature.training.TrainingScreen

@Composable
fun PeakProFitNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SplashNav,
        modifier = Modifier.fillMaxSize(),
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable<SplashNav> {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(HomeNav) {
                        popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(LoginNav) {
                        popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<LoginNav> { LoginScreen() }

        composable<HomeNav> {
            HomeScreen(
                onLoggedOut = {
                    navController.navigate(LoginNav) {
                        popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<TrainingNav> { TrainingScreen() }
        composable<ProgressNav> { ProgressScreen() }
        composable<ProfileNav> { ProfileScreen() }
    }
}
