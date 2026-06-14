# Funcionalidades Principales

## Autenticacion

- Inicio de sesion con Firebase Auth.
- Registro de usuarios con email y contrasena.
- Recuperacion de contrasena por email con Firebase Auth.
- Modo invitado para entrar sin cuenta.
- Observacion reactiva de sesion mediante `AuthState`.
- Logout desde perfil.

## Catalogo de ejercicios

- Carga inicial de ejercicios desde ExerciseDB.
- Paginacion por `limit` y `offset`.
- Busqueda remota por nombre.
- Estados diferenciados de carga inicial, carga incremental, busqueda, error y vacio.
- Guardado o desguardado de ejercicios para usuarios autenticados.

## Detalle de ejercicio

- Consulta por id remoto.
- Informacion detallada de cuerpo, musculos, equipamiento, dificultad, categoria, descripcion e instrucciones.
- Carga de imagen desde `GET /image?exerciseId={id}&resolution=360`.
- Cache local de imagen en `cacheDir/exercise-images`.
- Placeholder si la imagen no esta disponible.

## Ejercicios guardados

- Persistencia local agrupada por `userId`.
- Observacion reactiva de guardados.
- Edicion de nota personal por ejercicio.
- Confirmacion antes de eliminar un guardado.

## Perfil

- Estado diferenciado para usuario autenticado e invitado.
- Acceso a ejercicios guardados.
- Acceso a ajustes.
- Cierre de sesion.
- Grabacion, reproduccion y eliminacion de audio motivacional local.

## Ajustes

- Cambio de idioma en tiempo de ejecucion.
- Idiomas soportados: espanol e ingles.
- Uso de `AppCompatDelegate.setApplicationLocales`.

## UI comun

- Componentes reutilizables para botones, cards, chips, campos de texto, top bars, dialogs y busqueda.
- Tema propio basado en Material 3.
- Recursos centralizados en XML para textos, colores, dimensiones y temas.
