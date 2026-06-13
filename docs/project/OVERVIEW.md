# Resumen del Proyecto

## Que es PeakProFit

PeakProFit es una aplicacion Android centrada en consulta y gestion personal de ejercicios. Permite iniciar sesion, consultar un catalogo remoto, buscar ejercicios por nombre, abrir un detalle completo, guardar ejercicios por usuario, anotar informacion personal y configurar preferencias basicas como el idioma.

El proyecto esta desarrollado en Kotlin con Jetpack Compose y sigue una arquitectura por capas para separar UI, logica de negocio y acceso a datos.

## Objetivo funcional

La app cubre un flujo principal de fitness:

- Entrar como usuario autenticado o como invitado.
- Explorar ejercicios desde ExerciseDB.
- Buscar ejercicios por nombre usando la API remota.
- Abrir el detalle de un ejercicio.
- Ver informacion de zona corporal, musculo objetivo, equipamiento, descripcion e instrucciones.
- Guardar ejercicios por usuario autenticado.
- Consultar ejercicios guardados y anadir notas.
- Grabar un audio motivacional local desde el perfil.
- Cambiar el idioma de la aplicacion entre espanol e ingles.

## Objetivo tecnico

El proyecto busca demostrar una aplicacion Android moderna con:

- Kotlin como lenguaje principal.
- Jetpack Compose como sistema de UI.
- ViewModel y StateFlow para estado de pantalla.
- Corrutinas para trabajo asincrono.
- Retrofit y OkHttp para red.
- Firebase Auth para autenticacion.
- DataStore para persistencia local reactiva.
- Hilt para inyeccion de dependencias.
- Recursos centralizados para textos, dimensiones, colores y tema.
- Separacion clara entre `feature`, `domain`, `data` y `core`.

## Modulos logicos

- `core`: navegacion, tema, componentes comunes y utilidades compartidas.
- `feature`: pantallas, ViewModels y estados de UI.
- `domain`: entidades, repositorios como contratos y casos de uso.
- `data`: implementaciones concretas de Firebase, ExerciseDB, DataStore y almacenamiento local.

## Pantallas principales

- `Splash`: decide si el usuario entra al flujo principal o al login.
- `Login`: inicio de sesion y acceso como invitado.
- `Register`: creacion de cuenta.
- `Exercises`: listado, paginacion y busqueda remota.
- `ExerciseDetail`: detalle e imagen del ejercicio.
- `Profile`: cuenta, guardados, ajustes y audio motivacional.
- `SavedExercises`: lista de ejercicios guardados y notas.
- `Settings`: cambio de idioma.

## Idea principal para explicar el proyecto

PeakProFit combina una app real de usuario final con decisiones tecnicas propias de una arquitectura mantenible: la UI no conoce detalles de red ni persistencia, el dominio define contratos y casos de uso, y la capa `data` concentra la integracion con servicios externos y almacenamiento local.
