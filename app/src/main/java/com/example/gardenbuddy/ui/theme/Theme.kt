package com.example.gardenbuddy.ui.theme

import android.app.Activity
import android.os.Build
import android.view.View
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView


//private val DarkColorScheme = darkColorScheme(
//    primary = darkGreen,           // Usa darkGreen come colore primario
//    onPrimary = Color.Black,       // Colore del testo/icon per contrasto sul colore primario
//    secondary = Color.Red, // Puoi personalizzare ulteriori colori
//    tertiary = Color.Red ,  // Puoi personalizzare ulteriori colori
//    background = Color.White,
//    surface = darkGreen
//)
//
//private val LightColorScheme = lightColorScheme(
//    primary = Color(0xFF4CAF50),          // Usa darkGreen come colore primario
//    onPrimary = Color(0xFFFFFFFF),
//    primaryContainer = Color(0xFFFFFFFF)
//    secondary = Color(0xFFC8E6C9), // Puoi personalizzare ulteriori colori
//    tertiary = Color(0xFFFFFFFF) ,  // Puoi personalizzare ulteriori colori
//    background = Color(0xFFFFFFFF),
//    surface = Color(0xFFFFFFFF)
//
//    /* Other default colors to override
//    background = Color(0xFFFFFBFE),
//    surface = Color(0xFFFFFBFE),
//    onPrimary = Color.White,
//    onSecondary = Color.White,
//    onTertiary = Color.White,
//    onBackground = Color(0xFF1C1B1F),
//    onSurface = Color(0xFF1C1B1F),
//    */
//)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF4CAF50),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFC8E6C9),
    secondary = Color(0xFF8BC34A),
    onSecondary = Color(0xFF000000),
    secondaryContainer = Color(0xFFDCEDC8),
    background = Color(0xFFF1F8E9),
    onBackground = Color.Black,
    surface = Color(0xFFFFFFFF),
    onSurface = Color.Black,
    error = Color(0xFFB00020),
    onError = Color(0xFFFFFFFF),
    tertiary = Color(0xFF000000)
)

// Dark Theme Color Scheme
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFA5D6A7),
    onPrimary = Color(0xFF000000),
    primaryContainer = Color(0xFF388E3C),
    secondary = Color(0xFF9CCC65),
    onSecondary = Color(0xFF000000),
    secondaryContainer = Color(0xFF558B2F),
    background = Color(0xFF303030),
    onBackground = Color(0xFFFFFFFF),
    surface = Color(0xFF424242),
    onSurface = Color(0xFFFFFFFF),
    error = Color(0xFFCF6679),
    onError = Color(0xFF000000)
)

@Composable
fun GardenBuddyTheme(
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

    SetStatusBarIcons(isLightTheme = !darkTheme)


    MaterialTheme(
      colorScheme = colorScheme,
      typography = Typography,
      content = content
    )
}

@Composable
fun SetStatusBarIcons(isLightTheme: Boolean) {
    val view = LocalView.current
    val activity = LocalContext.current as? Activity

    if (!view.isInEditMode && activity != null) {
        SideEffect {
            val window = activity.window
            if (isLightTheme) {
                // For light theme, set dark icons (i.e. light status bar)
                window.decorView.systemUiVisibility =
                    window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                // For dark theme, clear the flag to allow light icons (or default icons)
                window.decorView.systemUiVisibility =
                    window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
        }
    }
}

