package com.illera.peakprofit.core.navigation

import kotlinx.serialization.Serializable

@Serializable
data object SplashWrapper

@Serializable
data object LoginWrapper

@Serializable
data object RegisterWrapper

@Serializable
data object MainTabsWrapper

@Serializable
data object SavedExercisesWrapper

@Serializable
data object SettingsWrapper

@Serializable
data class ExerciseDetailWrapper(
    val exerciseId: String
)
