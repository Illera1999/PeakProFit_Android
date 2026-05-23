# PeakProFit Android - Estado del Proyecto

Fecha de actualización: 2026-05-23

## 1. Resumen ejecutivo
PeakProFit (Android) se encuentra en una fase de base técnica estable: proyecto inicial configurado con Kotlin + Jetpack Compose + Material 3, estructura mínima de navegación creada y dependencias modernas centralizadas por catálogo de versiones.

Actualmente, la aplicación compila con configuración estándar de plantilla y muestra una pantalla inicial de prueba (`Hello Android`).

## 2. Estado funcional actual
- Pantalla principal activa en `MainActivity` con Compose.
- Tema visual base (`PeakProFitTheme`) configurado y aplicado.
- Navegación preparada a nivel de dependencias y primer destino tipado (`StartNav`) creado.
- Sin flujo funcional de negocio implementado todavía (entrenamiento, progreso, perfiles, etc.).

## 3. Stack técnico y configuración
- Lenguaje: Kotlin `2.0.21`
- Build system: Gradle (Kotlin DSL)
- Android Gradle Plugin: `8.13.2`
- UI: Jetpack Compose + Material 3
- Navegación: `androidx.navigation:navigation-compose:2.9.8`
- SDK:
  - `minSdk = 26`
  - `targetSdk = 36`
  - `compileSdk = 36`
- JVM target: `11`

## 4. Estructura relevante
- `app/src/main/java/com/illera/peakprofit/MainActivity.kt`
- `app/src/main/java/com/illera/peakprofit/core/navigation/Screens.kt`
- `app/src/main/java/com/illera/peakprofit/ui/theme/*`
- `app/build.gradle.kts`
- `gradle/libs.versions.toml`

## 5. Situación de calidad y pruebas
- Tests de plantilla incluidos:
  - Unit test base (`ExampleUnitTest`)
  - Instrumented test base (`ExampleInstrumentedTest`)
- No existe aún suite de pruebas de dominio ni UI tests de flujos reales.

## 6. Deuda técnica identificada
- `Screens.kt` define un destino, pero no existe `NavHost` conectado en `MainActivity`.
- La pantalla actual sigue siendo de demo; falta arquitectura de features.
- Falta definir convenciones de paquetes por capa/feature para escalar el código.
- No hay documentación funcional de producto ni roadmap técnico dentro del repo.

## 7. Próximos pasos recomendados
1. Implementar `NavHost` y rutas base (Inicio, Entrenamiento, Progreso, Perfil).
2. Definir arquitectura por capas (UI, dominio, datos) y contratos mínimos.
3. Sustituir la UI de demo por una Home funcional con estado.
4. Añadir pruebas unitarias de casos de uso y pruebas UI de navegación.
5. Establecer pipeline de calidad (lint/format/tests) antes de crecer en features.

## 8. Criterio de “listo” para la siguiente iteración
La siguiente iteración debe cerrar con una navegación operativa entre al menos 3 pantallas y cobertura básica de pruebas en navegación + lógica de presentación.
