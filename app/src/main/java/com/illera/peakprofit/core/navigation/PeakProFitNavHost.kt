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
import androidx.navigation.toRoute
import com.illera.peakprofit.feature.main.tap.MainTabsScreen
import com.illera.peakprofit.feature.auth.login.LoginScreen
import com.illera.peakprofit.feature.auth.register.RegisterScreen
import com.illera.peakprofit.feature.auth.splash.SplashScreen
import com.illera.peakprofit.feature.main.exercise_detail.ExerciseDetailScreen

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
                        navController.navigate(MainTabsNav) {
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
                        navController.navigate(MainTabsNav) {
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
                        navController.navigate(MainTabsNav) {
                            popUpTo<AuthGraph> { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }

        navigation<MainGraph>(startDestination = MainTabsNav) {
            composable<MainTabsNav> {
                MainTabsScreen(
                    onLoggedOut = {
                        navController.navigate(LoginNav) {
                            popUpTo<MainGraph> { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onNavigateToLogin = {
                        navController.navigate(AuthGraph) {
                            popUpTo<MainGraph> { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onOpenExerciseDetail = { exerciseId ->
                        navController.navigate(ExerciseDetailNav(exerciseId = exerciseId))
                    }
                )
            }

            composable<ExerciseDetailNav> { backStackEntry ->
                val route = backStackEntry.toRoute<ExerciseDetailNav>()
                ExerciseDetailScreen(
                    exerciseId = route.exerciseId,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
