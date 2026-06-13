# Perfil y Audio Motivacional

## Objetivo

La pantalla de perfil muestra informacion de sesion y permite grabar un mensaje motivacional local para el usuario autenticado.

## Piezas principales

- `ProfileScreen`
- `ProfileViewModel`
- `ProfileUiState`
- `MotivationalAudioRepository`
- `LocalMotivationalAudioRepository`

## Estado de perfil

La pantalla distingue:

- Usuario autenticado.
- Usuario invitado.
- Estado sin sesion.
- Audio existente.
- Grabacion en curso.
- Reproduccion en curso.
- Mensajes de estado o error.

## Permiso de microfono

El permiso se declara en `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.RECORD_AUDIO" />
```

La peticion en tiempo de ejecucion se hace desde Compose con `rememberLauncherForActivityResult` y `ActivityResultContracts.RequestPermission`.

## Grabacion

La grabacion usa `MediaRecorder`:

- Fuente: microfono.
- Formato: MPEG-4.
- Encoder: AAC.
- Duracion maxima: 60 segundos.

Primero se graba en un archivo temporal. Si la grabacion termina correctamente, se confirma el archivo definitivo. Si falla, se descarta el temporal.

## Reproduccion

La reproduccion usa `MediaPlayer` leyendo el archivo local del usuario.

## Almacenamiento

Los audios se guardan en el sandbox privado de la app:

```text
filesDir/motivational_audio/{ownerKey}.m4a
```

`ownerKey` se sanitiza para evitar nombres de archivo invalidos.

## Casuisticas controladas

- Usuario invitado sin grabacion.
- Permiso denegado.
- Error al iniciar grabacion.
- Fin automatico por duracion maxima.
- Cancelacion de grabacion.
- Reproduccion terminada.
- Eliminacion confirmada.
