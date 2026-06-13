# UI con Compose y Material 3

## Enfoque

La interfaz esta construida con Jetpack Compose. Las pantallas se componen de funciones `@Composable` y consumen estados inmutables expuestos por ViewModels.

## Tema

El tema vive en:

- `core/theme/Theme.kt`
- `core/theme/Color.kt`
- `core/theme/Type.kt`

Se usa Material 3 como base y se define una paleta propia mediante `PeakTheme`.

## Recursos visuales

Los recursos se centralizan en XML:

- `colors.xml`
- `dimens.xml`
- `themes.xml`
- `strings.xml`
- `values-en/strings.xml`

Esto evita valores dispersos y facilita mantenimiento.

## Componentes reutilizables

Componentes transversales:

- `ConfirmDialog`
- `ScreenTopBar`
- `PeakBottomNavigationBar`
- `PeakButtons`
- `PeakChips`
- `PeakDetailHero`
- `PeakExerciseCard`
- `PeakInfoRow`
- `PeakSearchBar`
- `PeakSectionCard`
- `PeakText`
- `PeakTextField`

## Layouts usados

La app usa estructuras Compose comunes:

- `Column` para disposicion vertical.
- `Row` para elementos horizontales.
- `Box` para superposicion o centrado.
- `LazyColumn` para listas eficientes.
- `HorizontalPager` para tabs principales.
- `FlowRow` para chips adaptables.

## Estado local en UI

Se usa `remember` para estados efimeros de UI:

- Mostrar u ocultar dialogs.
- Visibilidad de contrasena.
- Estado de foco.

Se usa `rememberSaveable` cuando el estado debe sobrevivir a recreaciones:

- Back stack de navegacion.
- Nota editable por ejercicio guardado.

## Dialogs

La app encapsula `AlertDialog` en `ConfirmDialog`. Esto permite reutilizar el mismo patron en:

- Confirmar salida de la app.
- Confirmar eliminacion de ejercicio guardado.
- Confirmar eliminacion de audio motivacional.

## Regla de UI

La UI no debe ejecutar llamadas de red ni persistencia directamente. La pantalla delega acciones al ViewModel y pinta segun el `UiState`.
