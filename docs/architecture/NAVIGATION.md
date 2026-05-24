# Navegacion

## Enfoque actual

Se usa `navigation-compose` con rutas tipadas y `NavHost` dedicado en `core/navigation/PeakProFitNavHost.kt`.

## Piezas principales

- `MainActivity`
Monta tema y delega toda la navegacion al host.

- `Screens.kt`
Define destinos tipados serializables:
- `SplashNav`
- `StartNav`
- `HomeNav`
- `TrainingNav`
- `ProgressNav`
- `ProfileNav`

- `PeakProFitNavHost.kt`
Declara el grafo y transiciones entre pantallas.

## Flujo inicial activo

1. `SplashNav`
2. `StartNav`
3. `HomeNav`

Y accesos directos a:
- `TrainingNav`
- `ProgressNav`
- `ProfileNav`

## Criterios para nuevas rutas

1. Crear destino tipado en `Screens.kt`.
2. Añadir `composable<Destino>()` en `PeakProFitNavHost`.
3. Encapsular argumentos en el propio destino tipado.
4. Mantener la logica de navegacion fuera de componentes UI puros.
