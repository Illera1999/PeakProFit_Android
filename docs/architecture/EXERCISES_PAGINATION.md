# Paginacion y Busqueda de Ejercicios

## Objetivo

La pestaña de ejercicios tiene dos modos de trabajo:

- Listado paginado cuando el buscador esta vacio.
- Busqueda remota por nombre cuando el usuario escribe texto.

Esta separacion evita mezclar una busqueda real de API con un filtro local incompleto.

## Listado paginado

## Endpoint

```text
GET /exercises
```

Parametros usados:

- `limit`
- `offset`
- `sortMethod = bodyPart`
- `sortOrder = ascending`

## Estado asociado

En `ExercisesUiState`:

- `isLoading`: carga inicial.
- `isLoadingMore`: carga de una pagina adicional.
- `hasMore`: indica si puede haber mas paginas.
- `items`: lista acumulada.

## Flujo

1. Al abrir la pestaña, `loadInitialExercises()` carga la primera pagina.
2. `currentOffset` se actualiza con el numero de elementos recibidos.
3. Al acercarse al final del `LazyColumn`, la UI llama a `loadMore()`.
4. El ViewModel evita cargas duplicadas si ya esta cargando o si `hasMore` es `false`.
5. Los resultados se concatenan y se deduplican por `id`.

## Criterio de fin

```text
hasMore = itemsRecibidos.size == PAGE_SIZE
```

Si la API devuelve menos elementos que el tamano de pagina, se considera final de lista.

## Busqueda remota por nombre

## Endpoint

```text
GET /exercises/name/{name}
```

## Estado asociado

En `ExercisesUiState`:

- `query`: texto actual del buscador.
- `isSearching`: carga de busqueda remota.
- `searchResults`: resultados de la busqueda.
- `visibleItems`: lista derivada que devuelve `searchResults` si hay query o `items` si no hay query.

## Flujo

1. El usuario escribe en `PeakSearchBar`.
2. `ExercisesViewModel.onQueryChanged()` actualiza `query`.
3. Se aplica un debounce de 350 ms.
4. Se ejecuta `SearchExercisesByNameUseCase`.
5. El repositorio llama a `ExerciseDbApi.searchExercisesByName`.
6. La UI muestra resultados, estado vacio o error de busqueda.

## Limpieza del buscador

Al limpiar `query`:

- Se cancela la busqueda pendiente.
- Se limpian los resultados de busqueda.
- Se vuelve al listado paginado ya cargado en `items`.

## Reintento

`retry()` decide segun el modo actual:

- Si hay query, reintenta la busqueda remota.
- Si no hay query y no hay items, reintenta la carga inicial.
- Si no hay query y ya hay items, reintenta `loadMore()`.

## Estados visuales

- Carga inicial: spinner central.
- Carga incremental: footer con progreso.
- Busqueda: spinner central.
- Error de listado: card con mensaje y reintento.
- Error de busqueda: card con mensaje y reintento.
- Lista vacia sin query: mensaje de catalogo vacio.
- Lista vacia con query: mensaje de resultados no encontrados.

## Motivo de la decision

La API soporta busqueda por nombre de forma remota. Usarla permite encontrar ejercicios que todavia no estan cargados en la paginacion local. Un filtro local solo podria buscar dentro de las paginas ya descargadas, lo que daria resultados incompletos.
