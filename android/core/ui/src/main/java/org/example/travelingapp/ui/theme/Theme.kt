package org.example.travelingapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryColor,
    secondary = AccentColor,
    tertiary = LilaOscuro,
    background = BackgroundColor,
    surface = CardBackgroundColor,
    onPrimary = White,
    onSecondary = Black,
    onBackground = PrimaryTextColor,
    onSurface = PrimaryTextColor,
    error = ErrorColor,
    secondaryContainer = Crema,
    tertiaryContainer = LightBlue
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    secondary = AccentColor,
    tertiary = LilaOscuro,
    background = BackgroundColor,
    surface = CardBackgroundColor,
    onPrimary = White,
    onSecondary = Black,
    onBackground = PrimaryTextColor,
    onSurface = PrimaryTextColor,
    error = ErrorColor,
    secondaryContainer = Crema,
    tertiaryContainer = LightBlue
)

@Composable
fun TravelingAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
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