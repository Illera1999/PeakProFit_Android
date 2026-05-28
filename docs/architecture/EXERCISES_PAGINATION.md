# Paginacion de Ejercicios

## Objetivo

Cargar la lista de ejercicios de forma incremental para:

- Reducir tiempo de primera pintura.
- Evitar descargas masivas innecesarias.
- Mantener una UX fluida en scroll.

## Contrato por capas

### Data

- Endpoint: `GET /exercises`
- Query params: `limit`, `offset`, `sortMethod`, `sortOrder`
- Tipo de respuesta actual: `List<ExerciseDto>`

### Domain

- Repositorio:
  - `suspend fun getExercises(limit: Int, offset: Int): List<Exercise>`
- Caso de uso:
  - `GetExercisesUseCase(limit, offset)`

### Presentation

- `ExercisesUiState`:
  - `isLoading`: carga inicial.
  - `isLoadingMore`: carga de pagina siguiente.
  - `hasMore`: indica si hay mas paginas candidatas.
  - `items`: acumulado completo.
  - `filteredItems`: resultado visible segun busqueda.

## Flujo de carga

1. Carga inicial:
- `limit = PAGE_SIZE`
- `offset = 0`
- Al completar, `currentOffset = items.size`.

2. Carga incremental:
- Se solicita `limit = PAGE_SIZE` con `offset = currentOffset`.
- Se concatena con el acumulado y se deduplica por `id`.
- Se incrementa `currentOffset` con el numero real recibido.

3. Corte de paginacion:
- `hasMore = (itemsRecibidos.size == PAGE_SIZE)`
- Si llega una pagina corta, se considera fin de lista.

## Reglas de seguridad

- No lanzar `loadMore()` si:
  - hay carga inicial en curso,
  - hay carga incremental en curso,
  - `hasMore` es `false`.
- La busqueda (`query`) se aplica localmente sobre lo ya cargado.
- Con `query` activa no se dispara auto-carga por scroll.

## Disparo desde UI

- En `LazyColumn`, al acercarse al final (umbral de 2 items), se llama `loadMore()`.
- Se muestra un footer de progreso durante `isLoadingMore`.
- Se muestra estado de fin cuando `hasMore == false`.

## Notas de mantenimiento

- La deduplicacion por `id` prioriza el primer elemento ya cargado.
- Si el proveedor cambia a cursor-based pagination, adaptar el contrato de dominio
  para `after/before` y mover la heuristica de fin a metadatos de API.
