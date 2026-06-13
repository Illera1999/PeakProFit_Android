# Ajustes e Idioma

## Objetivo

La pantalla de ajustes permite cambiar el idioma de la aplicacion en tiempo de ejecucion.

## Piezas principales

- `SettingsScreen`
- `SettingsViewModel`
- `SettingsUiState`
- `AppCompatDelegate`
- `LocaleListCompat`

## Idiomas soportados

- Espanol: `es`
- Ingles: `en`

## Flujo

1. `SettingsViewModel` lee el idioma actual.
2. `SettingsScreen` muestra las opciones con `RadioButton`.
3. El usuario selecciona idioma.
4. El ViewModel llama a `AppCompatDelegate.setApplicationLocales`.
5. Android recrea la actividad.
6. Compose vuelve a resolver `stringResource`.

## Recursos

Textos principales:

- `app/src/main/res/values/strings.xml`
- `app/src/main/res/values-en/strings.xml`

Configuracion de locales:

- `app/src/main/res/xml/locales_config.xml`

Manifest:

- `android:localeConfig="@xml/locales_config"`

## Motivo de usar AppCompat

`MainActivity` extiende `AppCompatActivity` para que el cambio de idioma por aplicacion funcione correctamente y la actividad se recree sin reinicio manual.

## Casuisticas controladas

- Locale vacio.
- Locale regional como `en-GB` o `es-ES`.
- Fallback a espanol si llega un idioma no soportado.
