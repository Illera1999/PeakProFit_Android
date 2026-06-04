# PeakProFit Android - Estado del Proyecto

Fecha de actualizacion: 2026-06-04

## 1. Resumen ejecutivo

El proyecto Android tiene una base funcional con autenticacion Firebase, navegacion tipada con grafos anidados, DI con Hilt y una primera integracion de ExerciseDB (listado y busqueda local).

## 2. Estado funcional actual

- `MainActivity` inicia tema y host de navegacion.
- Flujo auth operativo: `Splash -> Login -> Register -> Home`.
- Grafo principal operativo: `Home`, `Exercises`, `Training`, `Progress`, `Profile`.
- Login y registro separados por pantalla y ViewModel.
- Estado de sesion modelado con `AuthState` (`Loading`, `Authenticated`, `Unauthenticated`, `Error`).
- Manejo de credenciales con validacion minima y mapeo de errores Firebase a mensajes controlados.
- `Home` incluye dialogo de confirmacion al salir (`ConfirmDialog`).
- Integracion ExerciseDB via RapidAPI operativa para listado, detalle e imagen de ejercicios.
- La pantalla de detalle intenta cargar imagen con `GET /image?exerciseId={id}&resolution=360` y mantiene cache en memoria durante la ejecucion.
- La ausencia de imagen en algunos ejercicios no bloquea la carga del detalle.

## 3. Estructura de codigo actual

Ruta base: `app/src/main/java/com/illera/peakprofit`

- `core/navigation`, `core/theme`, `core/ui`
- `data/di`, `data/dto`, `data/remote`, `data/repository`
- `domain/entity`, `domain/repository`, `domain/usecase`
- `feature/splash`, `feature/login`, `feature/register`, `feature/home`
- `feature/exercises`, `feature/training`, `feature/progress`, `feature/profile`

## 4. Calidad y build

- Build verificado: `:app:assembleDebug` en verde.
- Tests actuales: base de plantilla (unit e instrumented).

## 5. Deuda tecnica pendiente

- No hay tests unitarios de `LoginViewModel`, `RegisterViewModel`, `ExercisesViewModel`.
- No hay cache local persistente para ejercicios e imagenes (Room/Disco). Solo existe cache en memoria por proceso.
- Falta externalizar todos los textos UI a `strings.xml`.

## 6. Siguiente iteracion recomendada

1. Añadir tests unitarios de autenticacion y ejercicios.
2. Implementar cache local para ejercicios e imagenes y estrategia offline basica.
3. Añadir placeholder visual cuando un ejercicio no tenga imagen disponible en API.
4. Añadir pipeline de calidad documentado (lint/format/tests).
