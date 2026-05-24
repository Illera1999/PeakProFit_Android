# Librerias y Plugins - Registro y Motivos

Este archivo registra plugins y librerias usados en el proyecto Android, el motivo de adopcion y el beneficio practico.

## Build y lenguaje

- `com.android.application` (AGP)
Motivo: plugin oficial para construir la aplicacion Android.
Beneficio: empaquetado, variantes y tareas de build estandar.

- `org.jetbrains.kotlin.android`
Motivo: soporte Kotlin para modulo Android.
Beneficio: Kotlin como lenguaje principal.

- `org.jetbrains.kotlin.plugin.compose`
Motivo: soporte del compilador para Jetpack Compose.
Beneficio: habilita composables y optimizaciones.

- `org.jetbrains.kotlin.plugin.serialization`
Motivo: rutas tipadas serializables en navegacion.
Beneficio: navegacion mas segura en compilacion.

- `com.google.devtools.ksp`
Motivo: procesamiento de codigo para Hilt.
Beneficio: generacion eficiente de codigo DI.

- `com.google.dagger.hilt.android`
Motivo: habilitar plugin de Hilt.
Beneficio: DI escalable en toda la app.

## UI

- `androidx.compose:compose-bom`
- `androidx.compose.ui:ui`
- `androidx.compose.ui:ui-graphics`
- `androidx.compose.ui:ui-tooling-preview`
- `androidx.compose.material3:material3`
- `androidx.activity:activity-compose`

Motivo: stack base Compose + Material3.
Beneficio: UI declarativa consistente y productiva.

## Arquitectura y estado

- `androidx.lifecycle:lifecycle-runtime-ktx`
- `androidx.lifecycle:lifecycle-runtime-compose`
- `androidx.lifecycle:lifecycle-viewmodel-compose`

Motivo: ciclo de vida + integracion ViewModel en Compose.
Beneficio: estado lifecycle-aware (`collectAsStateWithLifecycle`).

- `com.google.dagger:hilt-android`
- `com.google.dagger:hilt-compiler`
- `androidx.hilt:hilt-navigation-compose`

Motivo: DI y obtencion de ViewModels por destino.
Beneficio: desacople y wiring limpio.

## Navegacion

- `androidx.navigation:navigation-compose`

Motivo: definir grafo y rutas en Compose.
Beneficio: navegacion centralizada con grafos anidados (`AuthGraph`, `MainGraph`).

## Autenticacion y sesion

- `com.google.firebase:firebase-auth`

Motivo: login/registro y estado de sesion.
Beneficio: backend auth gestionado y estable.

- `org.jetbrains.kotlinx:kotlinx-coroutines-play-services`

Motivo: adaptar `Task` a corrutinas (`await()`).
Beneficio: implementaciones sin callbacks anidados.

## Red (ExerciseDB)

- `com.squareup.retrofit2:retrofit`
- `com.squareup.retrofit2:converter-gson`
- `com.squareup.okhttp3:logging-interceptor`

Motivo: integracion con ExerciseDB V1 (`oss.exercisedb.dev`).
Beneficio: cliente HTTP tipado, parseo JSON y trazabilidad de requests.

## Base Android

- `androidx.core:core-ktx`
Motivo: extensiones Kotlin para APIs Android.
Beneficio: codigo mas expresivo.

## Testing

- `junit:junit`
- `androidx.test.ext:junit`
- `androidx.test.espresso:espresso-core`
- `androidx.compose.ui:ui-test-junit4`
- `androidx.compose.ui:ui-test-manifest`
- `androidx.compose.ui:ui-tooling`

Motivo: stack base de pruebas JVM/instrumentadas/Compose.
Beneficio: cobertura de logica y UI.

## Estado actual de DI

- Patron activo: Hilt.
- Modulos activos: `AuthModule`, `ExerciseModule`.

## Regla de actualizacion

Cada vez que se añada o elimine una libreria:

1. Actualizar `gradle/libs.versions.toml`.
2. Actualizar `app/build.gradle.kts`.
3. Registrar el cambio y motivo en este documento.
