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
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = VerdePrincipal,
    secondary = VerdeOscuro,
    background = NegroTexto,
    surface = Color(0xFF2C2C2C),
    onPrimary = Blanco,
    onSecondary = Blanco,
    onBackground = GrisClaroFondo,
    onSurface = GrisClaroFondo,
    error = RojoError
)

private val LightColorScheme = lightColorScheme(
    primary = VerdePrincipal,
    secondary = VerdeOscuro,
    background = GrisClaroFondo,
    surface = Blanco,
    onPrimary = Blanco,
    onSecondary = Blanco,
    onBackground = NegroTexto,
    onSurface = NegroTexto,
    error = RojoError
)

@Composable
fun HuertoHogarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}