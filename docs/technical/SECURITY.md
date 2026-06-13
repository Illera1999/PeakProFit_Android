# Seguridad, Claves y Configuracion Local

## Principio general

Las claves reales no deben formar parte del repositorio. El proyecto usa `local.properties` para configuracion local y `.gitignore` para evitar subir secretos.

## Archivos que no se deben subir

- `local.properties`
- `google-services.json`
- `GoogleService-Info.plist`
- `.env`
- Keystores
- Service accounts
- Certificados privados
- Ficheros `.p8`, `.pem`, `.jks`, `.keystore`

## Archivo de ejemplo

`local.properties.example` documenta las claves necesarias sin incluir valores reales.

Ejemplo:

```properties
RAPID_API_KEY=YOUR_RAPIDAPI_KEY_HERE
RAPID_API_HOST=...
RAPID_API_BASE_URL=...
```

## RapidAPI

La clave se lee en Gradle:

```kotlin
localStringProperty("RAPID_API_KEY")
```

Luego se expone como `BuildConfig.RAPID_API_KEY` para que `ExerciseModule` la anada como cabecera.

## Firebase

Firebase requiere `google-services.json` en local. Ese archivo esta ignorado porque puede contener identificadores y configuracion del proyecto Firebase.

## Firma release

Los datos de firma se leen desde propiedades locales. No se deben versionar:

- ruta de keystore real
- passwords
- alias privado

## Revision antes de publicar

Antes de subir a GitHub:

```bash
git status --short
git ls-files | rg "local.properties|google-services.json|\\.jks|\\.keystore|\\.env"
```

Si el segundo comando devuelve resultados sensibles, no se debe publicar hasta retirarlos del tracking.

## Riesgo a tener en cuenta

`.gitignore` evita subir secretos nuevos, pero no elimina secretos que ya hayan sido versionados en commits anteriores. Si una clave se ha subido alguna vez, debe rotarse.
