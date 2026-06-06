# Localizacion y Textos UI

## Objetivo

Describir como se resuelven los textos visibles, como cambia el idioma en tiempo de ejecucion y que piezas participan en ese flujo.

## Idiomas soportados

- Espanol: `app/src/main/res/values/strings.xml`
- Ingles: `app/src/main/res/values-en/strings.xml`

La lista declarada al sistema esta en `app/src/main/res/xml/locales_config.xml`.

## Flujo de cambio de idioma

1. `SettingsScreen` muestra las opciones disponibles y delega el cambio en `SettingsViewModel`.
2. `SettingsViewModel` normaliza el tag (`es` o `en`) y llama a `AppCompatDelegate.setApplicationLocales(...)`.
3. `AppCompat` persiste la seleccion usando `AppLocalesMetadataHolderService` con `autoStoreLocales=true`.
4. La actividad activa se recrea y Compose vuelve a resolver los `stringResource(...)` con el locale nuevo.

## Requisitos tecnicos

- `MainActivity` extiende `AppCompatActivity` para integrarse con el mecanismo de recreacion de AppCompat.
- `AndroidManifest.xml` declara `android:localeConfig="@xml/locales_config"`.
- `AndroidManifest.xml` declara `AppLocalesMetadataHolderService` para persistir la preferencia de idioma entre reinicios.

## Textos desde ViewModel

- Los `ViewModel` no usan `stringResource`.
- Para errores o mensajes se usa `UiText`.
- `UiText.StringResource` permite emitir ids de recursos desde capa de presentacion sin acoplarse a Compose.
- La conversion final a texto visible se hace en UI con `UiText.asString()`.

## Validacion actual

- `LocaleChangeInstrumentedTest` verifica que el cambio a ingles y a espanol actualiza los recursos expuestos por `MainActivity`.

## Limitaciones actuales

- Solo hay dos idiomas soportados.
- Las opciones de idioma se construyen manualmente en `SettingsScreen`.
- No existe aun una estrategia de traduccion para textos procedentes de API remota.
