# Estado, Corrutinas y Flow

## ViewModel

Cada pantalla principal tiene un ViewModel que:

- Orquesta casos de uso.
- Expone estado con `StateFlow`.
- Ejecuta operaciones asincronas con `viewModelScope`.
- Traduce errores a `UiText` cuando deben mostrarse en UI.

## StateFlow

El patron habitual es:

```kotlin
private val _uiState = MutableStateFlow(FeatureUiState())
val uiState: StateFlow<FeatureUiState> = _uiState.asStateFlow()
```

La UI observa con:

```kotlin
val state by viewModel.uiState.collectAsStateWithLifecycle()
```

Esto respeta el ciclo de vida y evita colecciones activas cuando la pantalla no esta visible.

## Corrutinas

Se usan corrutinas para:

- Login y registro.
- Peticiones a ExerciseDB.
- Guardado en DataStore.
- Observacion de sesion.
- Grabacion y contador de audio motivacional.
- Busqueda remota con debounce.

## Flow

Se usa `Flow` o `StateFlow` en:

- Sesion de autenticacion.
- Ejercicios guardados.
- Comprobacion de si un ejercicio esta guardado.
- Estados de pantalla.

## UiState

Cada `UiState` contiene todo lo necesario para pintar la pantalla:

- Datos.
- Carga.
- Errores.
- Flags de permiso o autenticacion.
- Estados derivados cuando aporta claridad.

Ejemplo en `ExercisesUiState`:

- `isLoading`
- `isLoadingMore`
- `isSearching`
- `query`
- `items`
- `searchResults`
- `visibleItems`
- `errorMessage`

## Errores

Los errores visibles al usuario se modelan como `UiText`:

- `StringResource`
- texto dinamico si hiciera falta

Esto evita que los ViewModels dependan de `stringResource`, que solo existe en Compose.

## Debounce en busqueda

La busqueda de ejercicios espera 350 ms antes de llamar a la API. Esto reduce llamadas innecesarias y evita parpadeos durante escritura rapida.

## Criterio de mantenimiento

Si una operacion es asincrona y afecta a UI, debe reflejarse explicitamente en el `UiState`. Esto hace que la pantalla pueda representar correctamente carga, exito, error y vacio.
