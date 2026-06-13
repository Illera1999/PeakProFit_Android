# Variantes y Configuracion Gradle

## Configuracion base

Archivo principal:

```text
app/build.gradle.kts
```

Valores clave:

- `compileSdk = 36`
- `minSdk = 24`
- `targetSdk = 36`
- `applicationId = "com.illera.peakprofit"`
- `versionCode = 1`
- `versionName = "1.0"`

## Flavors

Dimension:

```text
environment
```

Flavors:

- `develop`
- `production`

## Build types

- `debug`
- `release`

## Variantes habilitadas

El proyecto filtra variantes con `androidComponents.beforeVariants`.

Variantes activas:

- `developDebug`
- `productionRelease`

Variantes desactivadas:

- `developRelease`
- `productionDebug`

## Develop

Caracteristicas:

- `ENVIRONMENT = "develop"`
- `versionNameSuffix = "-develop"`
- Puede leer `DEVELOP_RAPID_API_*`.
- Nombre visible: `PeakProFit Dev`.

## Production

Caracteristicas:

- `ENVIRONMENT = "production"`
- Puede leer `PRODUCTION_RAPID_API_*`.
- Nombre visible: `PeakProFit`.

## Debug

Caracteristicas:

- `versionNameSuffix = "-debug"`
- `isDebuggable = true`
- `ALLOW_LOGS = true`

## Release

Caracteristicas:

- `isDebuggable = false`
- `ALLOW_LOGS = false`
- Usa `localRelease`.
- `isMinifyEnabled = false` actualmente.

## Firma

La firma release puede configurarse con:

- `RELEASE_STORE_FILE`
- `RELEASE_STORE_PASSWORD`
- `RELEASE_KEY_ALIAS`
- `RELEASE_KEY_PASSWORD`

Si no existen, `localRelease` inicializa desde debug para permitir builds locales tempranas.

## Comandos utiles

```bash
./gradlew :app:assembleDevelopDebug
./gradlew :app:assembleProductionRelease
./gradlew :app:testDebugUnitTest
```
