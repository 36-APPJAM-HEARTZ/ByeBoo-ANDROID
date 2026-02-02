package com.byeboo.app.presentation.splash.termsofservice.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.noRippleClickable
import com.byeboo.app.core.util.screenHeightDp

@Composable
fun TermsCheckButton(
    modifier: Modifier = Modifier,
    title: String,
    isSelected: Boolean = false,
    hasMoreText: Boolean,
    onCheckClick: (Boolean) -> Unit,
    onLinkClick: () -> Unit
) {
    val contentColor = if (isSelected) ByeBooTheme.colors.gray50 else ByeBooTheme.colors.gray400

    Row(
        modifier =
        modifier
            .fillMaxWidth()
            .padding(vertical = screenHeightDp(8.dp))
            .noRippleClickable { onCheckClick(!isSelected) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_check),
            contentDescription = "check button",
            tint = contentColor,
            modifier =
            Modifier
                .size(12.dp)
        )

        Spacer(modifier = modifier.padding(horizontal = screenHeightDp(8.dp)))

        Text(
            text = title,
            style = ByeBooTheme.typography.cap2,
            color = contentColor
        )

        Spacer(modifier = modifier.weight(1f))

        if (hasMoreText) {
            Text(
                text = "더보기",
                style = ByeBooTheme.typography.cap2,
                color = contentColor,
                textDecoration = TextDecoration.Underline,
                modifier = modifier.noRippleClickable(onClick = onLinkClick)
            )
        }
    }
}
