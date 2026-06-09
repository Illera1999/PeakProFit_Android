package com.illera.peakprofit.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.material3.Text
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.illera.peakprofit.feature.auth.login.LoginScreen
import com.illera.peakprofit.feature.auth.register.RegisterScreen
import com.illera.peakprofit.feature.auth.splash.SplashScreen
import com.illera.peakprofit.feature.main.exercise_detail.ExerciseDetailScreen
import com.illera.peakprofit.feature.main.home.settings.SettingsScreen
import com.illera.peakprofit.feature.main.saved_exercises.SavedExercisesScreen
import com.illera.peakprofit.feature.main.tap.MainTabsScreen

@Composable
fun NavigationWrapper() {
    val backStack = rememberSaveable(saver = NavigationBackStackSaver) {
        mutableStateListOf<Any>(SplashWrapper)
    }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                SplashWrapper -> NavEntry(key) {
                    SplashScreen(
                        onNavigateToHome = { backStack.replaceWith(MainTabsWrapper) },
                        onNavigateToLogin = { backStack.replaceWith(LoginWrapper) }
                    )
                }

                LoginWrapper -> NavEntry(key) {
                    LoginScreen(
                        onNavigateToRegister = { backStack.add(RegisterWrapper) },
                        onNavigateToHome = { backStack.replaceWith(MainTabsWrapper) }
                    )
                }

                RegisterWrapper -> NavEntry(key) {
                    RegisterScreen(
                        onNavigateToLogin = { backStack.removeLastOrNull() },
                        onNavigateToHome = { backStack.replaceWith(MainTabsWrapper) }
                    )
                }

                MainTabsWrapper -> NavEntry(key) {
                    MainTabsScreen(
                        onLoggedOut = { backStack.replaceWith(LoginWrapper) },
                        onNavigateToLogin = { backStack.replaceWith(LoginWrapper) },
                        onOpenExerciseDetail = { exerciseId ->
                            backStack.add(ExerciseDetailWrapper(exerciseId))
                        },
                        onOpenSavedExercises = { backStack.add(SavedExercisesWrapper) },
                        onOpenSettings = { backStack.add(SettingsWrapper) }
                    )
                }

                SavedExercisesWrapper -> NavEntry(key) {
                    SavedExercisesScreen(
                        onBack = { backStack.removeLastOrNull() },
                        onOpenExerciseDetail = { exerciseId ->
                            backStack.add(ExerciseDetailWrapper(exerciseId))
                        }
                    )
                }

                SettingsWrapper -> NavEntry(key) {
                    SettingsScreen(
                        onBack = { backStack.removeLastOrNull() }
                    )
                }

                is ExerciseDetailWrapper -> NavEntry(key) {
                    ExerciseDetailScreen(
                        exerciseId = key.exerciseId,
                        onBack = { backStack.removeLastOrNull() }
                    )
                }

                else -> NavEntry(key) {
                    Text("Unknown destination")
                }
            }
        }
    )
}

private fun <T> MutableList<T>.replaceWith(destination: T) {
    clear()
    add(destination)
}

private val NavigationBackStackSaver = listSaver<SnapshotStateList<Any>, String>(
    save = { stack -> stack.map(::serializeNavigationKey) },
    restore = { saved ->
        mutableStateListOf<Any>().apply {
            addAll(saved.map(::deserializeNavigationKey))
        }
    }
)

private fun serializeNavigationKey(key: Any): String {
    return when (key) {
        SplashWrapper -> "splash"
        LoginWrapper -> "login"
        RegisterWrapper -> "register"
        MainTabsWrapper -> "main_tabs"
        SavedExercisesWrapper -> "saved_exercises"
        SettingsWrapper -> "settings"
        is ExerciseDetailWrapper -> "exercise_detail:${key.exerciseId}"
        else -> "splash"
    }
}

private fun deserializeNavigationKey(value: String): Any {
    return when {
        value == "splash" -> SplashWrapper
        value == "login" -> LoginWrapper
        value == "register" -> RegisterWrapper
        value == "main_tabs" -> MainTabsWrapper
        value == "saved_exercises" -> SavedExercisesWrapper
        value == "settings" -> SettingsWrapper
        value.startsWith("exercise_detail:") -> {
            ExerciseDetailWrapper(value.removePrefix("exercise_detail:"))
        }
        else -> SplashWrapper
    }
}
