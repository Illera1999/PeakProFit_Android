# Librerias y Plugins - Registro y Motivos

Este archivo registra plugins y librerias usados en el proyecto Android desde su inicio, el motivo de adopcion y el beneficio practico en el desarrollo.

## Build y lenguaje

- `com.android.application` (AGP)
Motivo: plugin oficial para construir la aplicacion Android.
Beneficio: empaquetado, variantes y tareas de build estandar para Android.

- `org.jetbrains.kotlin.android`
Motivo: soporte Kotlin para modulo Android.
Beneficio: permite usar Kotlin como lenguaje principal en toda la app.

- `org.jetbrains.kotlin.plugin.compose`
Motivo: soporte del compilador para Jetpack Compose.
Beneficio: habilita composables y optimizaciones del compilador para UI declarativa.

- `org.jetbrains.kotlin.plugin.serialization`
Motivo: habilitar rutas tipadas serializables en navegacion.
Beneficio: navegacion mas segura en compilacion y menor riesgo de errores con strings de rutas.

- `com.google.devtools.ksp`
Motivo: procesamiento de codigo para generacion de clases DI.
Beneficio: soporte eficiente para generadores como Hilt compiler.

- `com.google.dagger.hilt.android`
Motivo: habilitar plugin de Hilt en Android.
Beneficio: inyeccion de dependencias estandar y escalable en toda la app.

## UI

- `androidx.compose:compose-bom`
Motivo: alinear versiones de todo el ecosistema Compose.
Beneficio: evita incompatibilidades entre modulos Compose.

- `androidx.compose.ui:ui`
Motivo: primitives base de UI en Compose.
Beneficio: base para construir pantallas y componentes.

- `androidx.compose.ui:ui-graphics`
Motivo: APIs graficas usadas por componentes Compose.
Beneficio: soporte de dibujo, color y renderizado UI.

- `androidx.compose.ui:ui-tooling-preview`
Motivo: previews de componentes durante desarrollo.
Beneficio: iteracion rapida de UI sin ejecutar app completa.

- `androidx.compose.material3:material3`
Motivo: sistema de componentes Material 3.
Beneficio: componentes listos, consistentes y accesibles para UI productiva.

- `androidx.activity:activity-compose`
Motivo: integracion entre Activity y Compose (`setContent`).
Beneficio: punto de entrada limpio de Compose en Android.

## Arquitectura y estado

- `androidx.lifecycle:lifecycle-runtime-ktx`
Motivo: ciclo de vida y utilidades runtime para componentes Android.
Beneficio: mejor gestion de ciclo de vida y utilidades coroutine-friendly.

- `androidx.lifecycle:lifecycle-runtime-compose`
Motivo: APIs lifecycle-aware para Compose.
Beneficio: permite usar `collectAsStateWithLifecycle()` y evita colectar estado fuera del ciclo de vida visible.

- `androidx.lifecycle:lifecycle-viewmodel-compose`
Motivo: integracion de `ViewModel` en Compose (`viewModel()`).
Beneficio: acceso directo a ViewModel por pantalla con patron recomendado.

- `com.google.dagger:hilt-android`
Motivo: runtime de Hilt para resolver dependencias.
Beneficio: desacopla features de implementaciones concretas y simplifica wiring.

- `com.google.dagger:hilt-compiler`
Motivo: generacion de codigo necesaria para Hilt.
Beneficio: permite constructor injection y componentes DI en compilacion.

- `androidx.hilt:hilt-navigation-compose`
Motivo: integracion Hilt + Navigation Compose.
Beneficio: obtencion de ViewModel inyectado por destino de navegacion.

## Navegacion

- `androidx.navigation:navigation-compose`
Motivo: definir y ejecutar el grafo de navegacion en Compose.
Beneficio: grafo declarativo, transiciones y navegacion centralizada.

## Autenticacion y sesion

- `com.google.firebase:firebase-auth`
Motivo: gestionar inicio de sesion, registro y estado autenticado del usuario.
Beneficio: backend de autenticacion listo para produccion con gestion de sesion y metodos de login.

- `org.jetbrains.kotlinx:kotlinx-coroutines-play-services`
Motivo: adaptar `Task` de Google/Firebase a corrutinas (`await()`).
Beneficio: implementaciones de login/registro mas limpias, sin callbacks anidados.

## Base Android

- `androidx.core:core-ktx`
Motivo: extensiones Kotlin para APIs base de Android.
Beneficio: codigo mas expresivo y menos verboso en APIs Android.

## Testing

- `junit:junit`
Motivo: tests unitarios JVM.
Beneficio: validacion rapida de logica sin dispositivo/emulador.

- `androidx.test.ext:junit`
Motivo: soporte JUnit para tests instrumentados Android.
Beneficio: base estandar para tests que dependen de framework Android.

- `androidx.test.espresso:espresso-core`
Motivo: framework base para UI tests instrumentados.
Beneficio: automatizacion de interacciones de UI en entorno real Android.

- `androidx.compose.ui:ui-test-junit4`
Motivo: tests de UI especificos de Compose.
Beneficio: testeo directo del arbol Compose y semantica de componentes.

- `androidx.compose.ui:ui-test-manifest`
Motivo: soporte de manifest para tests Compose en debug.
Beneficio: configuracion correcta del entorno de pruebas Compose.

- `androidx.compose.ui:ui-tooling`
Motivo: herramientas de inspeccion y debug visual en desarrollo.
Beneficio: inspeccion de composables y soporte extra en debug build.

## Historial de incorporaciones

- Base inicial del proyecto:
  - AGP, Kotlin Android, Compose plugin, Compose BOM, Material3, Navigation Compose, Core KTX, Lifecycle Runtime KTX, Activity Compose y stack de tests.
- Navegacion tipada:
  - `org.jetbrains.kotlin.plugin.serialization`.
- Estado lifecycle-aware en Compose:
  - `androidx.lifecycle:lifecycle-runtime-compose`.
- Integracion de ViewModel en Compose:
  - `androidx.lifecycle:lifecycle-viewmodel-compose`.
- Autenticacion Firebase:
  - `com.google.firebase:firebase-auth`.
- Integracion Firebase con corrutinas:
  - `org.jetbrains.kotlinx:kotlinx-coroutines-play-services`.
- Migracion a DI con Hilt:
  - `com.google.dagger.hilt.android` (plugin)
  - `com.google.devtools.ksp` (plugin)
  - `com.google.dagger:hilt-android`
  - `com.google.dagger:hilt-compiler`
  - `androidx.hilt:hilt-navigation-compose`

## Estado actual de DI

- Patron activo: Hilt.
- Patron anterior: service locator manual eliminado.

## Checklist minimo Hilt

1. `@HiltAndroidApp` en la clase `Application`.
2. `android:name` en manifest apuntando a esa clase `Application`.
3. `@AndroidEntryPoint` en `MainActivity`.
4. Plugin Hilt + KSP activos en Gradle.
5. `hilt-android` + `hilt-compiler` + `hilt-navigation-compose` en dependencias.

## Regla de actualizacion

Cada vez que se añada o elimine una libreria:

1. Actualizar `gradle/libs.versions.toml`.
2. Actualizar `app/build.gradle.kts`.
3. Registrar el cambio y motivo en este documento.
