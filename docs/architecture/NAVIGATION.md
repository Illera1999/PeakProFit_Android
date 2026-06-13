# Navegacion

## Enfoque

La navegacion se implementa con Navigation 3 usando `NavDisplay` y una pila propia de destinos. El archivo principal es `core/navigation/NavigationWrapper.kt`.

La app no define rutas como strings sueltos para cada pantalla. En su lugar usa objetos wrapper y un back stack guardado con `rememberSaveable`.

## Destinos principales

- `SplashWrapper`
- `LoginWrapper`
- `RegisterWrapper`
- `MainTabsWrapper`
- `SavedExercisesWrapper`
- `SettingsWrapper`
- `ExerciseDetailWrapper(exerciseId)`

## Back stack

El back stack se guarda como `SnapshotStateList<Any>`:

```kotlin
val backStack = rememberSaveable(saver = NavigationBackStackSaver) {
    mutableStateListOf<Any>(SplashWrapper)
}
```

Para sobrevivir a recreaciones, cada destino se serializa a `String` y se restaura con `NavigationBackStackSaver`.

## Flujo de arranque

1. La app inicia en `SplashWrapper`.
2. `SplashScreen` observa la sesion.
3. Si hay usuario autenticado, navega a `MainTabsWrapper`.
4. Si no hay usuario, navega a `LoginWrapper`.

## Flujo de autenticacion

- `LoginWrapper` permite ir a `RegisterWrapper`.
- `RegisterWrapper` permite volver a login.
- Login correcto, registro correcto o modo invitado sustituyen la pila por `MainTabsWrapper`.

## Flujo principal

`MainTabsWrapper` contiene `MainTabsScreen`.

La pantalla principal usa:

- `HorizontalPager` para alternar entre tabs.
- `PeakBottomNavigationBar` como barra inferior.
- Tab `Exercises`.
- Tab `Profile`.

## Pantallas fuera de tabs

Desde el flujo principal se pueden abrir:

- `ExerciseDetailWrapper(exerciseId)`.
- `SavedExercisesWrapper`.
- `SettingsWrapper`.

Estas pantallas se apilan por encima de las tabs y vuelven con `removeLastOrNull()`.

## Back del sistema

En `MainTabsScreen`, el boton atras abre `ConfirmDialog` para confirmar salida de la app. Se declara despues del pager para dar prioridad a ese manejador.

## Criterios para nuevas pantallas

1. Crear un nuevo wrapper de navegacion.
2. Anadirlo al `entryProvider` de `NavigationWrapper`.
3. Anadir serializacion y deserializacion si debe sobrevivir a recreaciones.
4. Pasar callbacks de navegacion desde la pantalla padre.
5. Evitar que componentes UI puros conozcan la pila de navegacion.
