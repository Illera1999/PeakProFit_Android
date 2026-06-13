import java.util.Properties

plugins {
    // Plugin Android principal del módulo app.
    // Docs oficiales: https://developer.android.com/build
    alias(libs.plugins.android.application)
    // Compose compiler plugin (alineado con versión de Kotlin del catálogo).
    // Docs oficiales: https://developer.android.com/jetpack/compose/compiler
    alias(libs.plugins.kotlin.compose)
    // Soporte de serialización Kotlin (rutas tipadas/navigation y payloads serializables).
    // Docs oficiales: https://kotlinlang.org/docs/serialization.html
    alias(libs.plugins.kotlin.serialization)
    // Procesado de google-services.json (Firebase).
    // Docs oficiales: https://developers.google.com/android/guides/google-services-plugin
    alias(libs.plugins.google.services)
    // Inyección de dependencias con Hilt.
    // Docs oficiales: https://dagger.dev/hilt/
    alias(libs.plugins.hilt.android)
    // Procesado de anotaciones para Hilt (KSP).
    // Docs oficiales: https://kotlinlang.org/docs/ksp-overview.html
    alias(libs.plugins.kotlin.ksp)
}

// Carga propiedades locales no versionadas (claves API, rutas SDK, etc.).
val localProps = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) file.inputStream().use { this.load(it) }
}

fun localStringProperty(name: String, defaultValue: String = ""): String {
    return (localProps[name] as? String) ?: defaultValue
}

val defaultRapidApiKey = localStringProperty("RAPID_API_KEY")
val defaultRapidApiHost = localStringProperty(
    name = "RAPID_API_HOST",
    defaultValue = "exercisedb.p.rapidapi.com"
)
val defaultRapidApiBaseUrl = localStringProperty(
    name = "RAPID_API_BASE_URL",
    defaultValue = "https://exercisedb.p.rapidapi.com/"
)
val releaseStoreFile = localStringProperty("RELEASE_STORE_FILE")
val hasReleaseSigning =
    releaseStoreFile.isNotBlank() &&
        localStringProperty("RELEASE_STORE_PASSWORD").isNotBlank() &&
        localStringProperty("RELEASE_KEY_ALIAS").isNotBlank() &&
        localStringProperty("RELEASE_KEY_PASSWORD").isNotBlank()

android {
    namespace = "com.illera.peakprofit"
    // API de compilación usada por el toolchain.
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.illera.peakprofit"
        // Mínimo Android soportado por la app en producción.
        minSdk = 24
        // API objetivo para comportamiento y validaciones modernas del sistema.
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Valores remotos configurables por entorno.
        // Si no existen en local.properties se usan defaults seguros para desarrollo.
        // Se exponen en BuildConfig para consumo centralizado en la capa data.
        buildConfigField("String", "RAPID_API_KEY", "\"$defaultRapidApiKey\"")
        buildConfigField("String", "RAPID_API_HOST", "\"$defaultRapidApiHost\"")
        buildConfigField("String", "RAPID_API_BASE_URL", "\"$defaultRapidApiBaseUrl\"")
    }

    flavorDimensions += "environment"

    productFlavors {
        create("develop") {
            dimension = "environment"
            versionNameSuffix = "-develop"
            buildConfigField("String", "ENVIRONMENT", "\"develop\"")
            buildConfigField(
                "String",
                "RAPID_API_KEY",
                "\"${localStringProperty("DEVELOP_RAPID_API_KEY", defaultRapidApiKey)}\""
            )
            buildConfigField(
                "String",
                "RAPID_API_HOST",
                "\"${localStringProperty("DEVELOP_RAPID_API_HOST", defaultRapidApiHost)}\""
            )
            buildConfigField(
                "String",
                "RAPID_API_BASE_URL",
                "\"${localStringProperty("DEVELOP_RAPID_API_BASE_URL", defaultRapidApiBaseUrl)}\""
            )
        }
        create("production") {
            dimension = "environment"
            buildConfigField("String", "ENVIRONMENT", "\"production\"")
            buildConfigField(
                "String",
                "RAPID_API_KEY",
                "\"${localStringProperty("PRODUCTION_RAPID_API_KEY", defaultRapidApiKey)}\""
            )
            buildConfigField(
                "String",
                "RAPID_API_HOST",
                "\"${localStringProperty("PRODUCTION_RAPID_API_HOST", defaultRapidApiHost)}\""
            )
            buildConfigField(
                "String",
                "RAPID_API_BASE_URL",
                "\"${localStringProperty("PRODUCTION_RAPID_API_BASE_URL", defaultRapidApiBaseUrl)}\""
            )
        }
    }

    signingConfigs {
        create("localRelease") {
            initWith(getByName("debug"))
        }

        if (hasReleaseSigning) {
            getByName("localRelease").apply {
                storeFile = rootProject.file(releaseStoreFile)
                storePassword = localStringProperty("RELEASE_STORE_PASSWORD")
                keyAlias = localStringProperty("RELEASE_KEY_ALIAS")
                keyPassword = localStringProperty("RELEASE_KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        debug {
            versionNameSuffix = "-debug"
            isDebuggable = true
            buildConfigField("boolean", "ALLOW_LOGS", "true")
        }
        release {
            // De momento sin shrinking/obfuscation para simplificar debug y distribución temprana.
            isMinifyEnabled = false
            isDebuggable = false
            signingConfig = signingConfigs.getByName("localRelease")
            buildConfigField("boolean", "ALLOW_LOGS", "false")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        // Compatibilidad Java del bytecode generado.
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        // UI declarativa con Compose.
        compose = true
        // Generación de clase BuildConfig con constantes de entorno.
        buildConfig = true
    }
}

androidComponents {
    beforeVariants { variantBuilder ->
        val environment = variantBuilder.productFlavors
            .find { (dimension, _) -> dimension == "environment" }
            ?.second

        val allowed = when (environment) {
            "develop" -> variantBuilder.buildType == "debug"
            "production" -> variantBuilder.buildType == "release"
            else -> true
        }

        variantBuilder.enable = allowed
    }
}

dependencies {
    // Capa remota.
    // Firebase Auth: https://firebase.google.com/docs/auth/android/start
    implementation(libs.firebase.auth)
    // kotlinx-coroutines-play-services: https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-play-services/
    implementation(libs.kotlinx.coroutines.play.services)
    // Retrofit: https://square.github.io/retrofit/
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    // OkHttp: https://square.github.io/okhttp/
    implementation(libs.okhttp.logging.interceptor)

    // Nav 3
    // Docs oficiales: https://developer.android.com/guide/navigation/navigation-3
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)

    // DI.
    // Hilt: https://dagger.dev/hilt/
    implementation(libs.hilt.android)
    // Hilt + Jetpack: https://developer.android.com/training/dependency-injection/hilt-jetpack
    implementation(libs.androidx.hilt.lifecycle.viewmodel.compose)
    ksp(libs.hilt.compiler)

    // Android + Compose base.
    // AndroidX Core / KTX: https://developer.android.com/jetpack/androidx/releases/core
    implementation(libs.androidx.core.ktx)
    // AppCompat: https://developer.android.com/jetpack/androidx/releases/appcompat
    implementation(libs.androidx.appcompat)
    // DataStore Preferences: https://developer.android.com/topic/libraries/architecture/datastore
    implementation(libs.androidx.datastore.preferences)
    // Coil Compose + GIF decoder.
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
    // Lifecycle: https://developer.android.com/jetpack/androidx/releases/lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // Activity Compose: https://developer.android.com/jetpack/androidx/releases/activity
    implementation(libs.androidx.activity.compose)
    // Jetpack Compose: https://developer.android.com/jetpack/compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)

    // Test.
    // JUnit 4: https://junit.org/junit4/
    testImplementation(libs.junit)
    // AndroidX Test: https://developer.android.com/training/testing
    androidTestImplementation(libs.androidx.junit)
    // Espresso: https://developer.android.com/training/testing/espresso
    androidTestImplementation(libs.androidx.espresso.core)
    // Jetpack Compose Testing: https://developer.android.com/jetpack/compose/testing
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
