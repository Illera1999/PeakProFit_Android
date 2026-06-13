# Guion para Video Explicativo

Este guion propone un orden claro para presentar el proyecto desde lo funcional hasta lo tecnico.

## 1. Presentacion del proyecto

Explicar:

- Nombre: PeakProFit.
- Objetivo: app de fitness para consultar, buscar y guardar ejercicios.
- Stack principal: Kotlin, Jetpack Compose, Firebase, ExerciseDB, DataStore.
- Arquitectura por capas.

Idea clave:

```text
La app esta pensada como un proyecto Android moderno donde la UI, el dominio y los datos estan separados.
```

## 2. Demo funcional rapida

Orden sugerido:

1. Abrir la app.
2. Mostrar login, registro y modo invitado.
3. Entrar al flujo principal.
4. Abrir pestaña de ejercicios.
5. Mostrar listado paginado.
6. Buscar un ejercicio por nombre.
7. Abrir detalle.
8. Guardar ejercicio con usuario autenticado.
9. Ver ejercicios guardados.
10. Anadir o modificar nota.
11. Abrir perfil.
12. Mostrar audio motivacional.
13. Abrir ajustes y cambiar idioma.

## 3. Estructura del proyecto

Mostrar carpetas:

- `core`
- `feature`
- `domain`
- `data`

Explicar:

- `feature` pinta UI y gestiona estado de pantalla.
- `domain` define entidades, contratos y casos de uso.
- `data` implementa Firebase, Retrofit, DataStore y ficheros.
- `core` contiene navegacion, tema y componentes comunes.

## 4. Arquitectura aplicada

Explicar el flujo de una accion:

```text
Screen -> ViewModel -> UseCase -> Repository contract -> Repository implementation -> API/DataStore/Firebase
```

Ejemplo recomendado:

- Buscar ejercicio por nombre.
- Guardar ejercicio.

## 5. Estado y Compose

Mostrar:

- `UiState`
- `MutableStateFlow`
- `collectAsStateWithLifecycle`
- `remember`
- `rememberSaveable`

Explicar:

- ViewModel expone estado.
- Compose observa estado.
- La UI se redibuja cuando cambia el estado.

## 6. Navegacion

Mostrar:

- `NavigationWrapper`
- `NavDisplay`
- wrappers de destino
- `MainTabsScreen`
- `HorizontalPager`
- `PeakBottomNavigationBar`

Explicar:

- Splash decide el flujo inicial.
- Login/Register pertenecen al flujo de auth.
- Tabs principales viven en `MainTabsWrapper`.
- Detalle, guardados y ajustes se apilan encima de tabs.

## 7. ExerciseDB y red

Mostrar:

- `ExerciseDbApi`
- `ExerciseDbRepository`
- `ExerciseMapper`
- `ExerciseModule`

Explicar endpoints:

- `/exercises`
- `/exercises/name/{name}`
- `/exercises/exercise/{id}`
- `/image?exerciseId={id}&resolution=360`

Idea clave:

```text
La API devuelve DTOs; la app los transforma a entidades de dominio antes de usarlos en UI.
```

## 8. Persistencia

Mostrar:

- `StorageSavedExerciseRepository`
- `DataStore<Preferences>`
- `LocalMotivationalAudioRepository`

Explicar:

- Guardados por usuario en DataStore.
- Nota personal dentro del ejercicio guardado.
- Audio en almacenamiento privado.
- Imagenes de detalle en `cacheDir`.

## 9. Permisos y Android framework

Mostrar:

- `RECORD_AUDIO` en Manifest.
- Peticion con `rememberLauncherForActivityResult`.
- Uso de `MediaRecorder`.
- Uso de `MediaPlayer`.

Explicar:

- El permiso se solicita en tiempo de ejecucion.
- La app graba primero en temporal.
- Si la grabacion falla, no pisa el audio anterior.

## 10. Variantes y configuracion

Mostrar:

- `develop`
- `production`
- `debug`
- `release`
- filtro de variantes
- `local.properties.example`

Explicar:

- `developDebug` para desarrollo.
- `productionRelease` para distribucion.
- claves fuera del repositorio.

## 11. Seguridad

Mostrar:

- `.gitignore`
- `local.properties.example`
- uso de `BuildConfig`

Explicar:

- No se suben claves reales.
- Firebase config vive localmente.
- RapidAPI key vive en `local.properties`.

## 12. Cierre

Puntos fuertes:

- UI moderna con Compose.
- Arquitectura separada por capas.
- Estado reactivo con StateFlow.
- Integracion real con API externa.
- Persistencia local con DataStore.
- Permisos en tiempo de ejecucion.
- Variantes de build.
- Documentacion organizada.

Posibles mejoras futuras:

- Tests unitarios de ViewModels.
- Cache persistente del catalogo.
- Limpieza controlada de cache de imagenes.
- Mas filtros remotos por zona, musculo o equipamiento.
