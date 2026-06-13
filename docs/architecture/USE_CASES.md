# Casos de Uso

## Papel de los casos de uso

Los casos de uso encapsulan acciones concretas del dominio. En este proyecto no contienen logica compleja, pero aportan claridad: el ViewModel no llama directamente a todos los metodos del repositorio, sino a operaciones con nombre funcional.

## Autenticacion

## `SignInUseCase`

Responsabilidad: iniciar sesion con email y contrasena.

Flujo:

1. `LoginViewModel` valida campos.
2. Ejecuta `SignInUseCase`.
3. El repositorio usa Firebase Auth.
4. El estado de sesion cambia a `Authenticated`.

## `RegisterUseCase`

Responsabilidad: crear un usuario nuevo.

Flujo:

1. `RegisterViewModel` valida email, contrasena y confirmacion.
2. Ejecuta `RegisterUseCase`.
3. Firebase crea la cuenta.
4. La app navega al flujo principal.

## `ContinueAsGuestUseCase`

Responsabilidad: activar modo invitado.

Este modo permite acceder a la app sin usuario Firebase, pero limita acciones como guardar ejercicios.

## `ObserveSessionUseCase`

Responsabilidad: exponer el estado de sesion como flujo observable.

Estados principales:

- `Loading`
- `Authenticated`
- `Unauthenticated`
- `Guest`
- `Error`

## Ejercicios remotos

## `GetExercisesUseCase`

Responsabilidad: obtener una pagina de ejercicios.

Parametros:

- `limit`
- `offset`

Se usa en el listado principal cuando no hay busqueda activa.

## `SearchExercisesByNameUseCase`

Responsabilidad: buscar ejercicios por nombre en la API remota.

Se usa cuando el usuario escribe en el buscador de la pestaña `Exercises`.

## `GetExerciseByIdUseCase`

Responsabilidad: obtener el detalle completo de un ejercicio.

Se usa en:

- `ExerciseDetailViewModel`.
- Guardado desde el listado cuando hace falta persistir el detalle completo.

## `GetExerciseImageByIdUseCase`

Responsabilidad: obtener el archivo local de imagen del ejercicio.

El repositorio descarga la imagen desde ExerciseDB y la guarda como `.gif` en cache local.

## Ejercicios guardados

## `SaveExerciseUseCase`

Responsabilidad: guardar un ejercicio para un usuario autenticado.

## `RemoveSavedExerciseUseCase`

Responsabilidad: eliminar un ejercicio guardado.

## `ObserveSavedExercisesUseCase`

Responsabilidad: observar la lista reactiva de ejercicios guardados de un usuario.

## `IsExerciseSavedUseCase`

Responsabilidad: saber si un ejercicio concreto esta guardado.

Se usa para pintar correctamente el icono de guardado.

## `GetSavedExerciseByIdUseCase`

Responsabilidad: recuperar un ejercicio guardado concreto.

## `UpdateSavedExerciseNoteUseCase`

Responsabilidad: actualizar la nota personal de un ejercicio guardado.

## Criterio para crear nuevos casos de uso

Crear un caso de uso nuevo cuando:

- La accion representa una intencion de negocio clara.
- La misma accion puede ser usada por mas de un ViewModel.
- Conviene aislar al ViewModel de detalles del repositorio.
- La operacion tendra validaciones o reglas propias.
