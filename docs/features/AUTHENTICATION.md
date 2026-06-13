# Autenticacion y Sesion

## Objetivo

El flujo de autenticacion permite usar la app con cuenta Firebase o entrar como invitado. La sesion se modela como estado de dominio para que la UI no dependa directamente de Firebase.

## Piezas principales

- `FirebaseAuthRepository`
- `AuthRepository`
- `AuthState`
- `SignInUseCase`
- `RegisterUseCase`
- `ContinueAsGuestUseCase`
- `ObserveSessionUseCase`
- `SplashViewModel`
- `LoginViewModel`
- `RegisterViewModel`

## Estados de sesion

`AuthState` representa:

- `Loading`: la app aun esta resolviendo la sesion.
- `Authenticated`: hay usuario Firebase.
- `Unauthenticated`: no hay usuario.
- `Guest`: usuario invitado.
- `Error`: hubo un problema resolviendo sesion.

## Flujo de Splash

1. `SplashScreen` observa `AuthState`.
2. Si el estado es `Authenticated`, navega al flujo principal.
3. Si el estado es `Unauthenticated` o `Error`, navega a login.

## Login

1. El usuario introduce email y contrasena.
2. `LoginViewModel` valida campos obligatorios.
3. Ejecuta `SignInUseCase`.
4. Firebase Auth valida credenciales.
5. La navegacion sustituye el flujo de auth por tabs principales.

## Registro

1. El usuario introduce email, contrasena y confirmacion.
2. `RegisterViewModel` valida formato, longitud y coincidencia.
3. Ejecuta `RegisterUseCase`.
4. Firebase Auth crea la cuenta.

## Modo invitado

El modo invitado permite explorar la app sin cuenta. Algunas acciones quedan limitadas:

- No se pueden guardar ejercicios.
- No se muestra acceso real a guardados por usuario.
- El perfil muestra estado de invitado y accion para iniciar sesion.

## Gestion de errores

Los ViewModels traducen errores de Firebase a `UiText`, evitando que la capa UI contenga logica de excepciones.

Ejemplos:

- Email no valido.
- Usuario no encontrado.
- Credenciales incorrectas.
- Red no disponible.
- Demasiados intentos.

## Motivo de la decision

Firebase Auth se usa para evitar construir un backend propio de autenticacion. La app mantiene una capa de dominio encima para no acoplar pantallas a clases de Firebase.
