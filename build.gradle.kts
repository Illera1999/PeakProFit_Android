// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // AGP: https://developer.android.com/build
    alias(libs.plugins.android.application) apply false
    // Kotlin Android: https://kotlinlang.org/docs/android-overview.html
    alias(libs.plugins.kotlin.android) apply false
    // Compose compiler plugin: https://developer.android.com/jetpack/compose/compiler
    alias(libs.plugins.kotlin.compose) apply false
    // Hilt Gradle plugin: https://dagger.dev/hilt/gradle-setup.html
    alias(libs.plugins.hilt.android) apply false
    // KSP Gradle plugin: https://kotlinlang.org/docs/ksp-overview.html
    alias(libs.plugins.kotlin.ksp) apply false
}
