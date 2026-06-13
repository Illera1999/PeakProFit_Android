# Calidad, Pruebas y Verificacion

## Estado actual

El proyecto compila correctamente con:

```bash
./gradlew :app:assembleDevelopDebug
```

Hay pruebas base de JVM e instrumentadas. Tambien existe una prueba instrumentada enfocada en cambio de idioma.

## Tipos de pruebas disponibles

- Unit tests JVM en `app/src/test`.
- Instrumented tests en `app/src/androidTest`.
- Tests de Compose disponibles por dependencia.

## Comandos utiles

```bash
./gradlew :app:testDebugUnitTest
./gradlew :app:connectedDebugAndroidTest
./gradlew :app:lintDebug
./gradlew :app:assembleDevelopDebug
```

## Que deberia probarse a futuro

## ViewModels

- `LoginViewModel`
- `RegisterViewModel`
- `ExercisesViewModel`
- `ExerciseDetailViewModel`
- `SavedExercisesViewModel`
- `ProfileViewModel`
- `SettingsViewModel`

## Repositorios

- Mapeo de DTO a dominio.
- Errores de ExerciseDB.
- Persistencia y observacion de guardados.
- Escritura y lectura de audio local.

## UI

- Estados de carga.
- Estados de error.
- Busqueda sin resultados.
- Usuario invitado frente a usuario autenticado.
- Cambio de idioma.

## Criterio de calidad

Antes de publicar cambios relevantes:

1. Compilar `developDebug`.
2. Ejecutar unit tests si el cambio toca logica.
3. Ejecutar instrumentados si el cambio toca Android framework, permisos, DataStore o idioma.
4. Revisar que no haya secretos en staging.
5. Actualizar documentacion si cambia arquitectura, dependencias o comportamiento.
