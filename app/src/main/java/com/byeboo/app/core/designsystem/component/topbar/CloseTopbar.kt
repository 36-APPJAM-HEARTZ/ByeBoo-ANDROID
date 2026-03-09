package com.byeboo.app.core.designsystem.component.topbar

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.noRippleClickable

@Composable
fun CloseTopbar(
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    textColor: Color = ByeBooTheme.colors.white,
    textStyle: TextStyle = ByeBooTheme.typography.sub1,
) {
    ByeBooTopbar(
        title = title,
        textColor = textColor,
        textStyle = textStyle,
        actions = {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_cancel),
                contentDescription = null,
                tint = ByeBooTheme.colors.white,
                modifier =
                    Modifier.noRippleClickable(
                        onClick = onCloseClick,
                    ),
            )
        },
        modifier = modifier,
    )
}
