# Librerias Escogidas

Este documento explica que librerias y plugins usa PeakProFit y por que encajan en el proyecto.

## Plugins de build

## Android Gradle Plugin

Identificador:

```text
com.android.application
```

Motivo: construir, empaquetar y firmar la aplicacion Android.

## Kotlin Android

Identificador:

```text
org.jetbrains.kotlin.android
```

Motivo: usar Kotlin como lenguaje principal.

## Kotlin Compose

Identificador:

```text
org.jetbrains.kotlin.plugin.compose
```

Motivo: integrar el compilador de Compose con Kotlin.

## Kotlin Serialization

Identificador:

```text
org.jetbrains.kotlin.plugin.serialization
```

Motivo: soporte de serializacion Kotlin usado por piezas modernas del stack.

## Google Services

Identificador:

```text
com.google.gms.google-services
```

Motivo: procesar `google-services.json` y conectar Firebase.

## Hilt

Identificador:

```text
com.google.dagger.hilt.android
```

Motivo: habilitar inyeccion de dependencias con Hilt.

## KSP

Identificador:

```text
com.google.devtools.ksp
```

Motivo: procesar anotaciones de Hilt de forma eficiente.

## UI

## Jetpack Compose

Dependencias principales:

- `androidx.compose:compose-bom`
- `androidx.compose.ui:ui`
- `androidx.compose.ui:ui-graphics`
- `androidx.compose.material3:material3`
- `androidx.activity:activity-compose`

Motivo: construir UI declarativa, modular y reactiva.

Beneficio: las pantallas se describen como funciones de estado, encajando con ViewModel y StateFlow.

## Material Icons Extended

Dependencia:

```text
androidx.compose.material:material-icons-extended
```

Motivo: iconos para acciones comunes como perfil, fitness, busqueda, guardado y ajustes.

## AppCompat

Dependencia:

```text
androidx.appcompat:appcompat
```

Motivo: soportar cambio de idioma por aplicacion con `AppCompatDelegate`.

## Arquitectura y ciclo de vida

## Lifecycle

Dependencias:

- `androidx.lifecycle:lifecycle-runtime-ktx`
- `androidx.lifecycle:lifecycle-runtime-compose`
- `androidx.lifecycle:lifecycle-viewmodel-compose`

Motivo: ViewModel, ciclo de vida y observacion segura desde Compose.

Beneficio: `collectAsStateWithLifecycle` evita observar flujos cuando la UI no esta activa.

## Hilt

Dependencias:

- `com.google.dagger:hilt-android`
- `com.google.dagger:hilt-compiler`
- `androidx.hilt:hilt-lifecycle-viewmodel-compose`

Motivo: inyectar repositorios, clientes y ViewModels sin construir dependencias manualmente en pantallas.

## Navegacion

Dependencias:

- `androidx.navigation3:navigation3-runtime`
- `androidx.navigation3:navigation3-ui`

Motivo: usar Navigation 3 con `NavDisplay` y back stack controlado desde Compose.

Beneficio: navegacion declarativa, pila serializable y control claro de destinos.

## Red

## Retrofit

Dependencias:

- `com.squareup.retrofit2:retrofit`
- `com.squareup.retrofit2:converter-gson`

Motivo: definir endpoints HTTP como interfaz Kotlin.

Beneficio: llamadas tipadas y parseo automatico a DTOs.

## OkHttp Logging Interceptor

Dependencia:

```text
com.squareup.okhttp3:logging-interceptor
```

Motivo: registrar requests y responses en desarrollo.

Beneficio: facilita depurar ExerciseDB y confirmar endpoints.

## Firebase

## Firebase Auth

Dependencia:

```text
com.google.firebase:firebase-auth
```

Motivo: autenticacion con email y contrasena sin backend propio.

## Coroutines Play Services

Dependencia:

```text
org.jetbrains.kotlinx:kotlinx-coroutines-play-services
```

Motivo: convertir `Task` de Firebase a corrutinas con `await()`.

## Persistencia

## DataStore Preferences

Dependencia:

```text
androidx.datastore:datastore-preferences
```

Motivo: persistir ejercicios guardados con API reactiva basada en Flow.

Beneficio: mejor integracion con corrutinas que SharedPreferences.

## Imagenes

## Coil Compose

Dependencias:

- `io.coil-kt:coil-compose`
- `io.coil-kt:coil-gif`

Motivo: cargar imagenes desde archivos locales en Compose y registrar soporte para GIFs.

Beneficio: el detalle de ejercicio puede renderizar imagenes descargadas desde ExerciseDB.

## Testing

Dependencias:

- `junit:junit`
- `androidx.test.ext:junit`
- `androidx.test.espresso:espresso-core`
- `androidx.compose.ui:ui-test-junit4`
- `androidx.compose.ui:ui-test-manifest`
- `androidx.compose.ui:ui-tooling`

Motivo: cubrir pruebas JVM, instrumentadas y UI Compose.

## Regla de actualizacion

Cuando se anada, cambie o elimine una dependencia:

1. Actualizar `gradle/libs.versions.toml`.
2. Actualizar `app/build.gradle.kts`.
3. Actualizar este documento.
4. Verificar build.
