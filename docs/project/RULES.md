# Reglas de Mantenimiento

## Regla de arquitectura

- `feature` puede depender de `domain` y `core`.
- `domain` no debe depender de `data`, `feature` ni Android UI.
- `data` implementa contratos definidos en `domain`.
- `core` contiene piezas transversales, no reglas de negocio de una feature concreta.

## Regla para nuevas pantallas

Cada pantalla nueva debe seguir el patron:

- `XScreen.kt`
- `XViewModel.kt`
- `XUiState.kt`

Si la pantalla crece, se extraen componentes internos a `components/`.

## Regla para nuevos datos remotos

1. Definir DTO en `data/dto`.
2. Definir endpoint en `data/remote`.
3. Mapear DTO a entidad de dominio en `data/mapper`.
4. Exponer contrato en `domain/repository`.
5. Implementar repositorio en `data/repository`.
6. Crear caso de uso en `domain/usecase`.
7. Consumir desde ViewModel.

## Regla para estado UI

- El estado de pantalla vive en un `UiState` inmutable.
- El ViewModel expone `StateFlow`.
- Compose observa con `collectAsStateWithLifecycle`.
- Estados locales pequenos de UI pueden usar `remember` o `rememberSaveable`.

## Regla para strings y recursos

- No escribir textos fijos en pantallas si el texto es visible al usuario.
- Anadir texto en `values/strings.xml`.
- Anadir traduccion en `values-en/strings.xml`.
- Dimensiones reutilizables deben ir a `dimens.xml`.
- Colores base deben vivir en `colors.xml` y en el tema.

## Regla para secretos

- No subir `local.properties`.
- No subir `google-services.json`.
- No subir keystores ni certificados.
- No subir `.env`, service accounts ni claves privadas.
- Las claves reales deben vivir solo en archivos locales ignorados por Git.

## Regla para documentacion

Cada cambio relevante debe actualizar al menos uno de estos documentos:

- Arquitectura si cambia la separacion por capas.
- Feature correspondiente si cambia comportamiento de usuario.
- Librerias si se anade o elimina una dependencia.
- Seguridad si cambia la forma de gestionar claves.
- Calidad si cambia la forma de validar el proyecto.
