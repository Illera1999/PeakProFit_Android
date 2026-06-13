# Checklist Tecnico del Proyecto

Este documento resume las capacidades tecnicas implementadas y donde se pueden localizar en el codigo.

## Base Android

- `minSdk = 24`: definido en `app/build.gradle.kts`.
- `targetSdk = 36`: definido en `app/build.gradle.kts`.
- AndroidX: usado en Compose, Lifecycle, AppCompat, DataStore, Activity y testing.
- Kotlin: lenguaje principal del proyecto.
- Jetpack Compose: la UI principal esta implementada en Compose.
- Material 3: tema, componentes base y tipografia.

## UI Compose

- Uso de `Column`, `Row`, `Box` y `LazyColumn` en pantallas y componentes.
- Uso de `HorizontalPager` en `MainTabsScreen`.
- Uso de `remember`, `rememberSaveable` y estados locales en UI.
- Uso de `AlertDialog` encapsulado en `ConfirmDialog`.
- Componentes reutilizables:
  - `PeakSearchBar`
  - `PeakSectionCard`
  - `PeakPrimaryButton`
  - `PeakTextField`
  - `PeakChipRow`
  - `PeakBottomNavigationBar`
  - `PeakDetailHero`
  - `ScreenTopBar`

## Estado y ciclo de vida

- ViewModels por pantalla principal.
- `MutableStateFlow` y `StateFlow` para estados de UI.
- `collectAsStateWithLifecycle` para observar estado desde Compose.
- Corrutinas con `viewModelScope.launch`.
- Funciones `suspend` en repositorios y casos de uso.
- `Flow` en guardados y estado de sesion.

## Navegacion

- Navigation 3 con `NavDisplay` y back stack propio guardado con `rememberSaveable`.
- Flujo de autenticacion separado del flujo principal.
- Tabs principales con `HorizontalPager` y bottom navigation.
- Detalle de ejercicio como destino con parametro `exerciseId`.

## Datos y persistencia

- Retrofit + Gson para ExerciseDB.
- OkHttp con interceptor para cabeceras de RapidAPI.
- Firebase Auth para login y registro.
- DataStore Preferences para ejercicios guardados.
- Ficheros privados para audio motivacional.
- Cache local en `cacheDir` para imagenes de ejercicios.

## Settings y recursos

- Pantalla de ajustes funcional.
- Cambio de idioma como parametro funcional.
- Accion de ajustes accesible desde `Profile`.
- Recursos centralizados:
  - `strings.xml`
  - `values-en/strings.xml`
  - `dimens.xml`
  - `colors.xml`
  - `themes.xml`

## Permisos

- `INTERNET` para ExerciseDB y Firebase.
- `RECORD_AUDIO` para grabar el mensaje motivacional.
- Peticion de permiso en tiempo de ejecucion con `rememberLauncherForActivityResult`.

## Variantes

- Flavor `develop`.
- Flavor `production`.
- Build type `debug`.
- Build type `release`.
- Variantes habilitadas por filtro:
  - `developDebug`
  - `productionRelease`

## Seguridad

- Claves de RapidAPI desde `local.properties`.
- `google-services.json` ignorado por Git.
- Keystores, `.env` y service accounts ignorados por Git.
- Plantilla segura en `local.properties.example`.

## Calidad

- Build verificada con `./gradlew :app:assembleDevelopDebug`.
- Tests disponibles:
  - unitarios JVM base
  - instrumentados Android
  - prueba de cambio de idioma
