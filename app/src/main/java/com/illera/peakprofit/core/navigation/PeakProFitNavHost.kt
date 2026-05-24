package com.illera.peakprofit.core.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.illera.peakprofit.feature.exercises.ExercisesScreen
import com.illera.peakprofit.feature.home.HomeScreen
import com.illera.peakprofit.feature.login.LoginScreen
import com.illera.peakprofit.feature.register.RegisterScreen
import com.illera.peakprofit.feature.splash.SplashScreen

@Composable
fun PeakProFitNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AuthGraph,
        modifier = Modifier.fillMaxSize(),
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        navigation<AuthGraph>(startDestination = SplashNav) {
            composable<SplashNav> {
                SplashScreen(
                    onNavigateToHome = {
                        navController.navigate(HomeNav) {
                            popUpTo<AuthGraph> { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onNavigateToLogin = {
                        navController.navigate(LoginNav) {
                            popUpTo<SplashNav> { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable<LoginNav> {
                LoginScreen(
                    onNavigateToRegister = {
                        navController.navigate(RegisterNav) {
                            launchSingleTop = true
                        }
                    },
                    onNavigateToHome = {
                        navController.navigate(HomeNav) {
                            popUpTo<AuthGraph> { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable<RegisterNav> {
                RegisterScreen(
                    onNavigateToLogin = { navController.popBackStack() },
                    onNavigateToHome = {
                        navController.navigate(HomeNav) {
                            popUpTo<AuthGraph> { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }

        navigation<MainGraph>(startDestination = HomeNav) {
            composable<HomeNav> {
                HomeScreen(
                    onNavigateToExercises = { navController.navigate(ExercisesNav) },
                    onLoggedOut = {
                        navController.navigate(LoginNav) {
                            popUpTo<MainGraph> { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable<ExercisesNav> { ExercisesScreen() }
        }
    }
}
