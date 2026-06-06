# PeakProFit Android - Estado del Proyecto

Fecha de actualizacion: 2026-06-06

## 1. Resumen ejecutivo

El proyecto Android tiene una base funcional con autenticacion Firebase, navegacion tipada con grafos anidados, DI con Hilt, integracion de ExerciseDB (listado, detalle e imagen), guardado persistente de ejercicios por usuario y cambio de idioma en tiempo de ejecucion.

## 2. Estado funcional actual

- `MainActivity` inicia tema y host de navegacion.
- Flujo auth operativo: `Splash -> Login -> Register -> Home`.
- Grafo principal operativo: `Exercises`, `Home`, `SavedExercises`, `Settings`, `ExerciseDetail`.
- Login y registro separados por pantalla y ViewModel con formulario reutilizable (`AuthForm`).
- Estado de sesion modelado con `AuthState` (`Loading`, `Authenticated`, `Unauthenticated`, `Error`).
- Manejo de credenciales con validacion minima y mapeo de errores Firebase a mensajes localizables mediante `UiText`.
- `Home` actua como entrada a acciones de cuenta: logout, acceso a guardados y ajustes.
- `MainTabsScreen` incluye dialogo de confirmacion al salir (`ConfirmDialog`).
- Integracion ExerciseDB via RapidAPI operativa para listado, detalle e imagen de ejercicios.
- La pantalla de detalle intenta cargar imagen con `GET /image?exerciseId={id}&resolution=360` y mantiene cache en memoria durante la ejecucion.
- La ausencia de imagen en algunos ejercicios no bloquea la carga del detalle.
- Los ejercicios guardados se persisten en `DataStore<Preferences>` agrupados por `userId`.
- La app soporta español e ingles y permite cambiar el idioma desde `Settings` sin reiniciar manualmente.
- Existe prueba instrumentada (`LocaleChangeInstrumentedTest`) para validar que el cambio de locale actualiza los recursos de la actividad.

## 3. Estructura de codigo actual

Ruta base: `app/src/main/java/com/illera/peakprofit`

- `core/navigation`, `core/theme`, `core/ui`
- `data/di`, `data/dto`, `data/remote`, `data/repository`
- `domain/entity`, `domain/repository`, `domain/usecase`
- `feature/auth/*`
- `feature/main/exercises`, `feature/main/exercise_detail`, `feature/main/home`, `feature/main/saved_exercises`, `feature/main/tap`
- `feature/settings`

## 4. Calidad y build

- Build verificado: `:app:compileDebugKotlin` en verde.
- Tests JVM verificados: `:app:testDebugUnitTest` en verde.
- Tests instrumentados relevantes disponibles: `LocaleChangeInstrumentedTest`.

## 5. Deuda tecnica pendiente

- No hay tests unitarios de `LoginViewModel`, `RegisterViewModel`, `ExercisesViewModel`, `SettingsViewModel`.
- No hay cache local persistente para catalogo de ejercicios ni imagenes (Room/disco). Solo existe persistencia de favoritos y cache en memoria del detalle.
- El selector de idioma solo soporta `es` y `en`; si crece el catalogo de idiomas hara falta abstraer la lista de opciones.

## 6. Siguiente iteracion recomendada

1. Añadir tests unitarios de autenticacion, ejercicios y ajustes.
2. Implementar cache local para catalogo e imagenes y estrategia offline basica.
3. Añadir placeholder visual cuando un ejercicio no tenga imagen disponible en API.
4. Documentar y automatizar pipeline de calidad completo (lint, unit tests, instrumented tests).
