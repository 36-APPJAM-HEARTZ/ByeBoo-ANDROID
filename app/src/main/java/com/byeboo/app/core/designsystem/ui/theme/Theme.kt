package com.byeboo.app.core.designsystem.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LocalByeBooColors =
    staticCompositionLocalOf<ByeBooColors> { error("No colors provided") }
private val LocalByeBooTypography =
    staticCompositionLocalOf<ByeBooTypography> { error("No typography provided") }

object ByeBooTheme {
    val colors: ByeBooColors
        @Composable
        @ReadOnlyComposable
        get() = LocalByeBooColors.current

    val typography: ByeBooTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalByeBooTypography.current
}

@Composable
fun ProvideByeBooColorsAndTypography(
    colors: ByeBooColors,
    typography: ByeBooTypography,
    content: @Composable () -> Unit
) {
    val provideColors = remember { colors.copy() }
    provideColors.update(colors)
    val provideTypography = remember { typography.copy() }
    provideTypography.update(typography)

    CompositionLocalProvider(
        LocalByeBooColors provides provideColors,
        LocalByeBooTypography provides provideTypography,
        content = content
    )
}

@Composable
fun ByeBooTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = ByeBooDarkColors()
    val typography = ByeBooTypography()

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = true
        }
    }
    ProvideByeBooColorsAndTypography(colors, typography) {
        MaterialTheme(
            content = content
        )
    }
}
