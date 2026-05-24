# PeakProFit Android - Estado del Proyecto

Fecha de actualizacion: 2026-05-23

## 1. Resumen ejecutivo

El proyecto Android ya no esta en estado plantilla. Actualmente tiene base tecnica funcional con Kotlin + Compose + Material 3, arquitectura por capas creada y navegacion tipada operativa.

## 2. Estado funcional actual

- `MainActivity` inicia tema y host de navegacion.
- `PeakProFitNavHost` contiene el flujo principal de pantallas.
- Flujo inicial activo: `Splash -> Start -> Home`.
- Features base creadas: `splash`, `start`, `home`, `training`, `progress`, `profile`.
- `Home` tiene estado inicial mock (`HomeUiState`) y contenido minimo.

## 3. Estructura de codigo actual

Ruta base: `app/src/main/java/com/illera/peakprofit`

- `core/navigation`
- `core/theme`
- `core/ui`
- `data/di`, `data/dto`, `data/repository`
- `domain/entity`, `domain/mapper`, `domain/repository`, `domain/usecase`
- `feature/home`, `feature/profile`, `feature/progress`, `feature/splash`, `feature/start`, `feature/training`

## 4. Calidad y build

- Build verificado: `:app:assembleDebug` en verde.
- Tests actuales: solo los de plantilla (`unit` e `instrumented`).

## 5. Deuda tecnica pendiente

- No hay inyeccion de dependencias ni capa de datos conectada.
- No hay casos de uso reales en `domain/usecase`.
- No hay tests de navegacion ni tests de ViewModel.
- No hay pipeline de calidad (lint/format/tests) documentado.

## 6. Siguiente iteracion recomendada

1. Portar `Home` a estado funcional real (sin mocks).
2. Definir contratos de repositorio en `domain/repository`.
3. Implementar repositorio inicial en `data/repository`.
4. Añadir tests unitarios de `HomeViewModel` y un test de navegacion.
