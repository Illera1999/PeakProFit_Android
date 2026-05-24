# Arquitectura Base

## Objetivo

Mantener una estructura clara para crecer por features sin mezclar responsabilidades.

## Capas

- `feature/*`
Responsable de UI y estado de presentacion por pantalla.

- `domain/*`
Responsable de reglas de negocio, entidades y contratos de repositorio.

- `data/*`
Responsable de implementaciones concretas (API, base de datos, mapeos, etc.).

- `core/*`
Responsable de elementos transversales: navegacion, tema, componentes UI comunes.

## Convencion por feature

Cada feature puede crecer con este patron minimo:

- `XScreen.kt`
- `XUiState.kt`
- `XViewModel.kt`

Cuando la feature crezca:

- `components/` para piezas UI reutilizables dentro de la feature.
- `model/` si necesita modelos de presentacion especificos.

## Regla de dependencias

- `feature` puede depender de `domain` y `core`.
- `domain` no depende de `feature` ni de `data`.
- `data` implementa contratos definidos en `domain`.

## Inyeccion de dependencias

- Se usa Hilt como mecanismo unico de DI.
- `Application` usa `@HiltAndroidApp`.
- `MainActivity` usa `@AndroidEntryPoint`.
- Los `ViewModel` usan `@HiltViewModel` con constructor injection.
- Los modulos de provision estan en `core/di/*` (actualmente `AuthModule`).
- El `AndroidManifest.xml` debe declarar `android:name=".PeakProFitApp"` en `<application>`.

## Limpieza de enfoque anterior

- Se elimino el service locator manual.
- No se usa `AppContainer` en ningun flujo actual.
- El cableado de repositorios y casos de uso vive en modulos Hilt.
