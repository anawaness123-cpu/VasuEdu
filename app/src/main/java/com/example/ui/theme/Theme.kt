package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

private val PreschoolLightColorScheme = lightColorScheme(
    primary = PreschoolAmber,
    onPrimary = Color.White,
    primaryContainer = PreschoolAmberLight,
    onPrimaryContainer = PreschoolAmberDark,
    secondary = MeadowTeal,
    onSecondary = Color.White,
    secondaryContainer = MeadowTealLight,
    onSecondaryContainer = Color(0xFF004D40),
    tertiary = BloomPink,
    onTertiary = Color.White,
    tertiaryContainer = BloomPinkLight,
    onTertiaryContainer = Color(0xFF880E4F),
    background = WarmCream,
    onBackground = TextDark,
    surface = Color.White,
    onSurface = TextDark,
    surfaceVariant = WarmCard,
    onSurfaceVariant = TextDark,
    outline = Color(0xFFFFCC80)
)

private val PreschoolDarkColorScheme = darkColorScheme(
    primary = PreschoolAmber,
    onPrimary = Color.Black,
    primaryContainer = PreschoolAmberDark,
    onPrimaryContainer = PreschoolAmberLight,
    secondary = MeadowTeal,
    onSecondary = Color.Black,
    tertiary = BloomPinkLight,
    onTertiary = Color.Black,
    background = Color(0xFF1E1E24),
    onBackground = Color(0xFFF0F0F0),
    surface = Color(0xFF282832),
    onSurface = Color(0xFFF0F0F0)
)

val PreschoolShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(36.dp)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep cheerful preschool branding consistent
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> PreschoolDarkColorScheme
        else -> PreschoolLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = PreschoolShapes,
        content = content
    )
}
