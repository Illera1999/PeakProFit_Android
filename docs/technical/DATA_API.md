# Datos Remotos y ExerciseDB

## Proveedor remoto

La app consume ExerciseDB a traves de RapidAPI. La configuracion se lee desde `BuildConfig`, generado con valores de `local.properties`.

Variables:

- `RAPID_API_KEY`
- `RAPID_API_HOST`
- `RAPID_API_BASE_URL`

## Cliente HTTP

Se usa:

- Retrofit para definir endpoints.
- Gson para convertir JSON a DTO.
- OkHttp para cliente HTTP e interceptores.
- HttpLoggingInterceptor en nivel `BASIC`.

## Cabeceras

`ExerciseModule` anade:

```text
X-RapidAPI-Key
X-RapidAPI-Host
```

La key solo se anade si no esta vacia.

## Endpoints usados

Listado:

```text
GET /exercises
```

Detalle:

```text
GET /exercises/exercise/{id}
```

Busqueda por nombre:

```text
GET /exercises/name/{name}
```

Imagen:

```text
GET /image?exerciseId={id}&resolution=360
```

## DTO y mapeo

La API devuelve `ExerciseDto`. La app lo transforma a `Exercise` con `ExerciseMapper`.

Motivo:

- La estructura remota puede cambiar.
- La entidad de dominio debe mantenerse limpia.
- La UI consume un modelo estable.

## Cache en memoria

`ExerciseDbRepository` mantiene cache por id durante la ejecucion:

- Evita repetir llamadas de detalle.
- Permite fusionar informacion parcial de listado con detalle.

## Imagenes

El endpoint de imagen devuelve bytes de GIF. La app:

1. Descarga bytes con Retrofit.
2. Guarda el archivo en `cacheDir/exercise-images`.
3. Devuelve un `File` al dominio.
4. La UI lo renderiza con Coil.

## Fallback de imagen

Si `/image` devuelve 404, el repositorio intenta usar `gifUrl` si existe en el ejercicio cacheado o recuperado por detalle.

## Errores esperados

- Falta de `RAPID_API_KEY`.
- Error de red.
- 404 en imagen.
- Cambios de esquema en respuesta JSON.
- Respuesta vacia.
