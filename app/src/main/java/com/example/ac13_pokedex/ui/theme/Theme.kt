package com.example.ac13_pokedex.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


private val LightColorScheme = lightColorScheme(
    primary = PokedexRed,
    onPrimary = Color.White,
    secondary = PokeBlue,
    tertiary = PokeYellow,
    secondaryContainer = Color(0xFFFFDAD6),
    onSecondaryContainer = Color(0xFF410006),
    background = LightBackground,
    surface = LightBackground,
    surfaceContainer = Color(0xFFF7ECEB),
)

private val DarkColorScheme = darkColorScheme(
    primary = PokedexRedDark,
    onPrimary = Color.White,
    secondary = Color(0xFFBAC3FF),
    tertiary = PokeYellow,
    secondaryContainer = Color(0xFF73000F),
    onSecondaryContainer = Color(0xFFFFDAD6),
    background = DarkBackground,
    surface = DarkBackground,
    surfaceContainer = Color(0xFF231E1F),
)

@Composable
fun Ac13_pokedexTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}