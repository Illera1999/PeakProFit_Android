import java.util.Properties

plugins {
    // Plugin Android principal del módulo app.
    alias(libs.plugins.android.application)
    // Compose compiler plugin (alineado con versión de Kotlin del catálogo).
    alias(libs.plugins.kotlin.compose)
    // Soporte de serialización Kotlin (rutas tipadas/navigation y payloads serializables).
    alias(libs.plugins.kotlin.serialization)
    // Procesado de google-services.json (Firebase).
    alias(libs.plugins.google.services)
    // Inyección de dependencias con Hilt.
    alias(libs.plugins.hilt.android)
    // Procesado de anotaciones para Hilt (KSP).
    alias(libs.plugins.kotlin.ksp)
}

// Carga propiedades locales no versionadas (claves API, rutas SDK, etc.).
val localProps = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) file.inputStream().use { this.load(it) }
}

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
        val rapidApiKey = (localProps["RAPID_API_KEY"] as? String) ?: ""
        val rapidApiHost = (localProps["RAPID_API_HOST"] as? String)
            ?: "exercisedb.p.rapidapi.com"
        val rapidApiBaseUrl = (localProps["RAPID_API_BASE_URL"] as? String)
            ?: "https://exercisedb.p.rapidapi.com/"

        // Se exponen en BuildConfig para consumo centralizado en la capa data.
        buildConfigField("String", "RAPID_API_KEY", "\"$rapidApiKey\"")
        buildConfigField("String", "RAPID_API_HOST", "\"$rapidApiHost\"")
        buildConfigField("String", "RAPID_API_BASE_URL", "\"$rapidApiBaseUrl\"")
    }

    buildTypes {
        release {
            // De momento sin shrinking/obfuscation para simplificar debug y distribución temprana.
            isMinifyEnabled = false
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

dependencies {
    // Navegación y capa remota.
    implementation(libs.androidx.navigation.compose)
    implementation(libs.firebase.auth)
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.logging.interceptor)

    // Nav 3
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)

    // DI.
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    // Android + Compose base.
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)

    // Test.
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
