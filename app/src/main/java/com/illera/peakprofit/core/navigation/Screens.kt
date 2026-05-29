package com.illera.peakprofit.core.navigation

import kotlinx.serialization.Serializable

@Serializable
data object AuthGraph

@Serializable
data object MainGraph

@Serializable
data object MainTabsNav

@Serializable
data object SplashNav

@Serializable
data object LoginNav

@Serializable
data object RegisterNav

@Serializable
data object HomeNav

@Serializable
data object ExercisesNav

@Serializable
data class ExerciseDetailNav(
    val exerciseId: String
)
