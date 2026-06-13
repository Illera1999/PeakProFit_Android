# Persistencia Local

## DataStore para ejercicios guardados

Los ejercicios guardados usan DataStore Preferences mediante `StorageSavedExerciseRepository`.

Se persiste un mapa:

```text
userId -> List<Exercise>
```

El valor se serializa como JSON con Gson.

## Por que DataStore

DataStore encaja mejor que SharedPreferences porque:

- Tiene API basada en corrutinas y Flow.
- Evita bloqueos en hilo principal.
- Facilita observacion reactiva.
- Es mas seguro para escrituras asincronas.

## Migracion

`ExerciseModule` configura `SharedPreferencesMigration` para conservar datos si existian en almacenamiento previo.

## Control de concurrencia

`StorageSavedExerciseRepository` usa `Mutex` para proteger escrituras y evitar condiciones de carrera al actualizar listas.

## Cache de imagenes

Las imagenes de detalle se guardan en:

```text
cacheDir/exercise-images/{id}-360.gif
```

Esta cache pertenece al almacenamiento temporal de la app. Android puede limpiarla si necesita espacio.

## Audio motivacional

El audio se guarda en:

```text
filesDir/motivational_audio/{ownerKey}.m4a
```

Se usa almacenamiento privado de la app para no requerir permisos de archivos externos.

## Archivos temporales

Durante grabacion se usa:

```text
{ownerKey}.tmp.m4a
```

Solo se reemplaza el audio definitivo si la grabacion termina correctamente.

## Datos que no se persisten

No hay cache persistente del catalogo completo de ejercicios. El listado se carga desde red en cada ejecucion y se mantiene en memoria durante la sesion.
