# Ejercicios Guardados

## Objetivo

La feature de ejercicios guardados permite que un usuario autenticado conserve ejercicios importantes y anada una nota personal.

## Piezas principales

- `SavedExercisesScreen`
- `SavedExercisesViewModel`
- `SavedExercisesUiState`
- `SavedExerciseRepository`
- `StorageSavedExerciseRepository`
- `ObserveSavedExercisesUseCase`
- `UpdateSavedExerciseNoteUseCase`
- `RemoveSavedExerciseUseCase`

## Persistencia

Los datos se guardan en DataStore Preferences. Internamente se persiste un mapa:

```text
userId -> List<Exercise>
```

El mapa se serializa con Gson.

## Observacion reactiva

`StorageSavedExerciseRepository.observeSavedExercises(userId)` devuelve un `Flow<List<Exercise>>`.

Esto permite que la UI se actualice automaticamente cuando:

- Se guarda un ejercicio.
- Se elimina un ejercicio.
- Se actualiza una nota.

## Notas personales

Cada ejercicio guardado puede tener `savedNote`. En la pantalla se edita con un campo de texto y se persiste al perder foco para evitar escrituras en cada tecla.

## Eliminacion

Antes de eliminar un ejercicio guardado se muestra `ConfirmDialog`. Esto evita borrar por error una nota personal.

## Casuisticas controladas

- Usuario no autenticado.
- Lista vacia.
- Error al actualizar guardado.
- Error al guardar nota.
- Eliminacion confirmada.
- Persistencia reactiva tras cambios.
