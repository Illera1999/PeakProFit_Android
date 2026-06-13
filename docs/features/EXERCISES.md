# Feature de Ejercicios

## Objetivo

La feature de ejercicios permite explorar el catalogo remoto, buscar por nombre, abrir detalles y guardar ejercicios para usuarios autenticados.

## Archivos principales

- `ExercisesScreen.kt`
- `ExercisesViewModel.kt`
- `ExercisesUiState.kt`
- `ExerciseCard.kt`
- `ExerciseDetailScreen.kt`
- `ExerciseDetailViewModel.kt`
- `ExerciseDetailUiState.kt`

## Listado

El listado usa `LazyColumn` y carga datos desde ExerciseDB por paginas. La UI observa `ExercisesUiState` y pinta segun el estado actual:

- Spinner inicial.
- Lista de ejercicios.
- Footer de carga.
- Fin de lista.
- Error con reintento.
- Estado vacio.

## Busqueda

La busqueda es remota por nombre:

```text
GET /exercises/name/{name}
```

El ViewModel aplica debounce para evitar llamadas excesivas mientras el usuario escribe.

## Guardado

Solo usuarios autenticados pueden guardar ejercicios.

El flujo es:

1. Usuario pulsa icono de guardar.
2. `ExercisesViewModel` comprueba si el ejercicio ya esta guardado.
3. Si no esta guardado, carga detalle si hace falta.
4. Ejecuta `SaveExerciseUseCase` o `RemoveSavedExerciseUseCase`.
5. DataStore emite la lista actualizada.

## Detalle

El detalle combina:

- Informacion textual del ejercicio.
- Chips de zona, musculo, equipamiento, dificultad y categoria.
- Imagen desde endpoint remoto.
- Boton de guardar o quitar guardado.
- Descripcion e instrucciones.

## Imagen del ejercicio

El endpoint usado es:

```text
GET /image?exerciseId={id}&resolution=360
```

La respuesta se guarda en cache local como `.gif`. La UI la carga desde un archivo local con Coil.

## Casuisticas controladas

- API sin clave configurada.
- Error de listado.
- Error de busqueda.
- Error de carga incremental.
- Busqueda sin resultados.
- Ejercicio sin imagen disponible.
- Usuario invitado sin permisos para guardar.
- Ejercicio ya guardado.
- Detalle enriquecido con cache en memoria por id e imagen almacenada en cache local.
