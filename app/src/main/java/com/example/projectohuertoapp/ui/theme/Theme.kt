package com.example.projectohuertoapp.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Esquema de colores para el tema CLARO, usando los colores de la web
private val LightColorScheme = lightColorScheme(
    primary = VerdePrincipalWeb,       // Color principal para botones, AppBar, etc.
    onPrimary = BlancoWeb,             // Texto sobre el color primario (ej. texto en botones)
    secondary = VerdeOscuroWeb,        // Color secundario (podría usarse para FABs o acentos)
    onSecondary = BlancoWeb,           // Texto sobre el color secundario
    background = FondoClaroWeb,        // Fondo principal de las pantallas
    onBackground = TextoPrincipalWeb,  // Texto sobre el fondo principal
    surface = BlancoWeb,               // Color de fondo para Cards, Menus, Dialogs
    onSurface = TextoPrincipalWeb,     // Texto sobre las superficies (ej. texto en Cards)
    error = RojoErrorWeb,              // Color para errores
    onError = BlancoWeb                // Texto sobre el color de error
    // Puedes definir otros colores si los necesitas (tertiary, surfaceVariant, etc.)
)

// Esquema de colores para el tema OSCURO
private val DarkColorScheme = darkColorScheme(
    primary = VerdePrincipalWeb,        // Mantenemos el verde principal
    onPrimary = BlancoWeb,              // Texto sobre primario sigue siendo blanco
    secondary = VerdeOscuroWeb,         // Mantenemos el verde secundario
    onSecondary = BlancoWeb,            // Texto sobre secundario sigue blanco
    background = FondoOscuroApp,        // Fondo oscuro general
    onBackground = TextoClaroSobreOscuro, // Texto claro sobre fondo oscuro
    surface = SuperficieOscuraApp,      // Fondo oscuro para Cards, etc.
    onSurface = TextoClaroSobreOscuro,  // Texto claro sobre superficies oscuras
    error = RojoErrorWeb,               // Mantenemos el rojo de error
    onError = BlancoWeb                 // Texto sobre error sigue blanco
    // Asegúrate que onBackground y onSurface tengan buen contraste
)

@Composable
fun HuertoHogarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    // dynamicColor: Boolean = true, // Puedes habilitar colores dinámicos si quieres
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        //     val context = LocalContext.current
        //     if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        // }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Cambia el color de la barra de estado
            window.statusBarColor = colorScheme.primary.toArgb()
            // Controla si los íconos de la barra de estado son claros u oscuros
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Asegúrate que tu archivo Type.kt esté definido
        content = content
    )
}
