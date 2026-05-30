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
Responsable de elementos transversales: navegacion, tema, componentes UI comunes.

## Convencion por feature

Cada feature puede crecer con este patron minimo:

- `XScreen.kt`
- `XUiState.kt`
- `XViewModel.kt`

Cuando la feature crezca:

- `components/` para piezas UI reutilizables dentro de la feature.
- `model/` si necesita modelos de presentacion especificos.

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

## Casos de uso activos

- Auth:
- `SignInUseCase`
- `RegisterUseCase`
- `ObserveSessionUseCase`

- Exercises:
- `GetExercisesUseCase`
- `GetExerciseByIdUseCase`

## Paginacion de ejercicios

- La carga de ejercicios se hace por paginas mediante `limit` y `offset`.
- El contrato de dominio para ejercicios expone `getExercises(limit, offset)`.
- El estado de pantalla separa carga inicial (`isLoading`) de carga incremental (`isLoadingMore`).
- La descripcion completa del flujo esta en [EXERCISES_PAGINATION.md](./EXERCISES_PAGINATION.md).

## Detalle de ejercicios

- El detalle usa endpoint dedicado por id (`GET /exercises/exercise/{id}`).
- La navegacion al detalle es una ruta tipada (`ExerciseDetailNav`) en el `MainGraph`.
- La carga de detalle se gestiona en `ExerciseDetailViewModel`.
- El repositorio de ejercicios mantiene cache en memoria por `id` para evitar llamadas repetidas durante la sesion.

## Notas de modelado

- La sesion se modela con `AuthState` en dominio para evitar ambiguedad de `null`.
- `Exercise` es entidad de dominio desacoplada del DTO remoto.
- El modo invitado se modela en sesion (`AuthState.Guest`) y se consume desde `ObserveSessionUseCase`.
