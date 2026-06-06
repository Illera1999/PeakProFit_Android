# Arquitectura Base

## Objetivo

Mantener una estructura clara para crecer por features sin mezclar responsabilidades.

## Capas

- `feature/*`
Responsable de UI y estado de presentacion por pantalla.

- `domain/*`
Responsable de reglas de negocio, entidades y contratos de repositorio.

- `data/*`
Responsable de implementaciones concretas (API, Firebase, mapeos, etc.).

- `core/*`
Responsable de elementos transversales: navegacion, tema, componentes UI comunes y utilidades de texto localizable.

## Convencion por feature

Cada feature puede crecer con este patron minimo:

- `XScreen.kt`
- `XUiState.kt`
- `XViewModel.kt`

Cuando la feature crezca:

- `components/` para piezas UI reutilizables dentro de la feature.
- `model/` si necesita modelos de presentacion especificos.
- `feature/auth/components/AuthForm.kt` es la referencia actual de extraccion de UI comun entre pantallas hermanas.

## Regla de dependencias

- `feature` puede depender de `domain` y `core`.
- `domain` no depende de `feature` ni de `data`.
- `data` implementa contratos definidos en `domain`.

## Inyeccion de dependencias

- Se usa Hilt como mecanismo unico de DI.
- `Application` usa `@HiltAndroidApp`.
- `MainActivity` usa `@AndroidEntryPoint`.
- Los `ViewModel` usan `@HiltViewModel` con constructor injection.
- Los modulos de provision estan en `data/di/*` (`AuthModule`, `ExerciseModule`).
- `AndroidManifest.xml` declara `android:name=".PeakProFitApp"` en `<application>`.
- `ExerciseModule` tambien registra `DataStore<Preferences>` para persistir ejercicios guardados por usuario.

## Casos de uso activos

- Auth:
- `SignInUseCase`
- `RegisterUseCase`
- `ObserveSessionUseCase`

- Exercises:
- `GetExercisesUseCase`
- `GetExerciseByIdUseCase`
- `GetExerciseImageByIdUseCase`
- `ObserveSavedExercisesUseCase`
- `GetSavedExerciseByIdUseCase`
- `IsExerciseSavedUseCase`
- `SaveExerciseUseCase`
- `RemoveSavedExerciseUseCase`

## Paginacion de ejercicios

- La carga de ejercicios se hace por paginas mediante `limit` y `offset`.
- El contrato de dominio para ejercicios expone `getExercises(limit, offset)`.
- El estado de pantalla separa carga inicial (`isLoading`) de carga incremental (`isLoadingMore`).
- La descripcion completa del flujo esta en [EXERCISES_PAGINATION.md](./EXERCISES_PAGINATION.md).

## Detalle de ejercicios

- El detalle usa endpoint dedicado por id (`GET /exercises/exercise/{id}`).
- La imagen del ejercicio usa endpoint dedicado con query params (`GET /image?exerciseId={id}&resolution=360`).
- No se usa la variante `GET /image/{id}` porque no coincide con la implementacion validada en iOS ni con el comportamiento real observado en RapidAPI.
- La navegacion al detalle es una ruta tipada (`ExerciseDetailNav`) en el `MainGraph`.
- La carga de detalle se gestiona en `ExerciseDetailViewModel`.
- El repositorio de ejercicios mantiene cache en memoria por `id` para evitar llamadas repetidas durante la sesion.
- La imagen se cachea en memoria durante la vida del proceso para evitar descargas repetidas en la misma ejecucion.
- Si la API no devuelve imagen para un ejercicio concreto, el detalle sigue siendo valido y la UI no debe tratarlo como error bloqueante.

## Persistencia local de guardados

- Los ejercicios guardados viven en `StorageSavedExerciseRepository`.
- El repositorio persiste un mapa `userId -> List<Exercise>` serializado con Gson dentro de `DataStore`.
- Las escrituras se protegen con `Mutex` para evitar sobreescrituras cuando hay operaciones concurrentes.
- `SharedPreferencesMigration` permite mover datos previos al nuevo almacenamiento sin perder favoritos.

## Localizacion y mensajes UI

- Los `ViewModel` emiten errores y textos mediante `UiText` para no depender de `stringResource`.
- Los recursos viven en `values/strings.xml` y `values-en/strings.xml`.
- El idioma activo se cambia con `AppCompatDelegate.setApplicationLocales(...)` desde `SettingsViewModel`.
- `MainActivity` usa `AppCompatActivity` para soportar la recreacion automática asociada a ese cambio de locale.
- El detalle del flujo esta en [LOCALIZATION.md](./LOCALIZATION.md).

## Notas de modelado

- La sesion se modela con `AuthState` en dominio para evitar ambiguedad de `null`.
- `Exercise` es entidad de dominio desacoplada del DTO remoto.
- El modo invitado se modela en sesion (`AuthState.Guest`) y se consume desde `ObserveSessionUseCase`.
