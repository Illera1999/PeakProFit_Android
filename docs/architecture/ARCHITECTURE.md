# Arquitectura Aplicada

## Objetivo

La arquitectura de PeakProFit busca separar responsabilidades para que el proyecto sea facil de explicar, mantener y ampliar. La idea principal es que la UI no conozca detalles de red, Firebase, DataStore ni almacenamiento de ficheros.

## Capas principales

## `feature`

Contiene pantallas, estados de UI y ViewModels.

Responsabilidades:

- Renderizar la interfaz con Compose.
- Exponer acciones de usuario al ViewModel.
- Observar `StateFlow` con `collectAsStateWithLifecycle`.
- Mostrar estados de carga, error, vacio y contenido.

Ejemplos:

- `feature/auth/login`
- `feature/main/exercises`
- `feature/main/exercise_detail`
- `feature/main/saved_exercises`
- `feature/main/profile`

## `domain`

Contiene el modelo de negocio independiente de Android UI.

Responsabilidades:

- Entidades de dominio.
- Contratos de repositorio.
- Casos de uso.
- Reglas que no deben depender de Retrofit, Firebase ni Compose.

Ejemplos:

- `Exercise`
- `AuthState`
- `ExerciseRepository`
- `SavedExerciseRepository`
- `GetExercisesUseCase`
- `SearchExercisesByNameUseCase`

## `data`

Contiene implementaciones concretas de acceso a datos.

Responsabilidades:

- Llamadas remotas con Retrofit.
- Integracion con Firebase Auth.
- Persistencia con DataStore.
- Acceso a ficheros locales.
- Mapeo entre DTOs remotos y entidades de dominio.

Ejemplos:

- `ExerciseDbRepository`
- `FirebaseAuthRepository`
- `StorageSavedExerciseRepository`
- `LocalMotivationalAudioRepository`

## `core`

Contiene elementos transversales reutilizables.

Responsabilidades:

- Navegacion.
- Tema visual.
- Componentes UI comunes.
- Utilidades como `UiText` y `Logger`.

Ejemplos:

- `NavigationWrapper`
- `PeakProFitTheme`
- `ConfirmDialog`
- `PeakSearchBar`
- `ScreenTopBar`

## Flujo de dependencia

La direccion esperada es:

```text
feature -> domain
feature -> core
data -> domain
app/di -> data + domain
```

`domain` queda en el centro y no depende de Android UI ni de implementaciones externas.

## Patron por pantalla

Las pantallas principales siguen este esquema:

```text
FeatureScreen.kt
FeatureViewModel.kt
FeatureUiState.kt
```

El objetivo es que:

- `Screen` se encargue de pintar.
- `ViewModel` se encargue de orquestar casos de uso.
- `UiState` represente el estado completo que necesita la pantalla.

## Inyeccion de dependencias

Se usa Hilt:

- `PeakProFitApp` esta anotada con `@HiltAndroidApp`.
- `MainActivity` esta anotada con `@AndroidEntryPoint`.
- Los ViewModels usan `@HiltViewModel`.
- Los modulos `AuthModule` y `ExerciseModule` proveen repositorios, Retrofit, OkHttp, Gson y DataStore.

## Ventajas de esta arquitectura

- Permite cambiar una implementacion de `data` sin tocar la UI.
- Los casos de uso hacen mas clara la intencion de cada operacion.
- El estado de pantalla queda centralizado.
- Es mas facil explicar el proyecto por bloques.
- Facilita pruebas futuras de ViewModels y casos de uso.

## Ejemplo aplicado: buscar ejercicios

1. `ExercisesScreen` envia texto al ViewModel.
2. `ExercisesViewModel` aplica debounce.
3. `SearchExercisesByNameUseCase` ejecuta la accion de dominio.
4. `ExerciseRepository` define el contrato.
5. `ExerciseDbRepository` llama a Retrofit.
6. `ExerciseDto` se mapea a `Exercise`.
7. El ViewModel actualiza `ExercisesUiState`.
8. Compose redibuja la lista visible.

## Ejemplo aplicado: guardar ejercicios

1. El usuario pulsa guardar en una card.
2. El ViewModel comprueba sesion y estado actual.
3. Se ejecuta `SaveExerciseUseCase` o `RemoveSavedExerciseUseCase`.
4. `StorageSavedExerciseRepository` actualiza DataStore.
5. `ObserveSavedExercisesUseCase` emite la lista actualizada.
6. La UI refleja el nuevo estado.
