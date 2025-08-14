package com.byeboo.app.presentation.splash.termsofservice.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.noRippleClickable

@Composable
fun TermsButton(
    modifier: Modifier = Modifier,
    title: String,
    isSelected: Boolean = false,
    hasMoreText: Boolean,
    onCheckClick: (Boolean) -> Unit,

) {

    val contentColor = if (isSelected) ByeBooTheme.colors.gray50 else ByeBooTheme.colors.gray400

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_check),
            contentDescription = "check button",
            tint = contentColor,
            modifier = Modifier
                .size(12.dp)
                .noRippleClickable{ onCheckClick(!isSelected) }
        )

        Spacer(modifier = modifier.padding(end = 16.dp))

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
                textDecoration = TextDecoration.Underline
            )
        }
    }
}

@Preview
@Composable
private fun TermsButtonPreview() {
    ByeBooTheme {
        var checked by remember { mutableStateOf(false) }
        TermsButton(title = "(필수) 서비스 이용약관 동의", isSelected = checked, hasMoreText = true, onCheckClick = {checked = it}, modifier = Modifier )
    }
}