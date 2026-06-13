# PeakProFit Android

PeakProFit es una aplicacion Android de fitness desarrollada con Kotlin y Jetpack Compose. El proyecto integra autenticacion con Firebase, catalogo de ejercicios mediante ExerciseDB en RapidAPI, busqueda remota por nombre, detalle de ejercicios con imagenes, guardado de ejercicios por usuario, audio motivacional local y soporte multiidioma.

La documentacion completa esta organizada en [`docs/README.md`](./docs/README.md).

## Resumen tecnico

- Kotlin + AndroidX.
- Jetpack Compose + Material 3.
- Arquitectura por capas: `feature`, `domain`, `data` y `core`.
- ViewModel, StateFlow, corrutinas y `collectAsStateWithLifecycle`.
- Hilt para inyeccion de dependencias.
- Retrofit + OkHttp + Gson para ExerciseDB.
- Firebase Auth para autenticacion.
- DataStore Preferences para persistencia local de ejercicios guardados.
- Variantes Gradle para `developDebug` y `productionRelease`.

## Configuracion local

1. Copiar `local.properties.example` como `local.properties`.
2. Completar `RAPID_API_KEY`, `RAPID_API_HOST` y `RAPID_API_BASE_URL`.
3. Anadir `google-services.json` localmente si se va a usar Firebase.
4. Compilar con:

```bash
./gradlew :app:assembleDevelopDebug
```

Los archivos con claves reales no deben subirse al repositorio.
