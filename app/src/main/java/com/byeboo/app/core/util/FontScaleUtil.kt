package com.byeboo.app.core.util

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density

@Composable
fun FixedFontScaleLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalDensity provides
            Density(
                density = LocalDensity.current.density,
                fontScale = 1f,
            ),
    ) {
        content()
    }
}

@Composable
fun FixedFontScaleText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    style: TextStyle = LocalTextStyle.current,
    textAlign: TextAlign? = null,
) {
    FixedFontScaleLayout {
        Text(
            text = text,
            modifier = modifier,
            color = color,
            style = style,
            textAlign = textAlign,
        )
    }
}
