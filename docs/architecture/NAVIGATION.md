# Navegacion

## Enfoque actual

Se usa `navigation-compose` con rutas tipadas y `NavHost` dedicado en `core/navigation/PeakProFitNavHost.kt`.

La navegacion esta separada en dos grafos:

- `AuthGraph`: flujo de autenticacion.
- `MainGraph`: flujo principal autenticado.

## Destinos tipados actuales

Definidos en `Screens.kt`:

- `AuthGraph`
- `MainGraph`
- `MainTabsNav`
- `SplashNav`
- `LoginNav`
- `RegisterNav`
- `HomeNav`
- `ExercisesNav`

## Flujo activo

1. App arranca en `AuthGraph` con `SplashNav`.
2. `SplashNav` decide:
- `Authenticated` -> `MainTabsNav` limpiando `AuthGraph`.
- `Unauthenticated/Error` -> `LoginNav`.
3. Desde `LoginNav` se puede navegar a `RegisterNav`.
4. Login/registro correctos navegan a `MainTabsNav` limpiando `AuthGraph`.
5. `MainTabsNav` contiene navegacion interna por tabs (`Exercises` y `Home`) con `HorizontalPager` + `NavigationBar`.
6. Logout desde `Home` navega a `LoginNav` limpiando `MainGraph`.

## Criterios para nuevas rutas

1. Crear destino tipado en `Screens.kt`.
2. Añadir `composable<Destino>()` en `PeakProFitNavHost` dentro del grafo correcto.
3. Si cambia de flujo (auth <-> main), limpiar el grafo origen con `popUpTo<Grafo>() { inclusive = true }`.
4. Mantener la logica de navegacion fuera de componentes UI puros.
