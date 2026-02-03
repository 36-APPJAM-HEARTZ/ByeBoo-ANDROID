package com.byeboo.app.presentation.mypage.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.noRippleClickable

@Composable
fun MyPageNotification(
    isEnabledAlarm: Boolean?,
    onCheckedClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val toggle = if (isEnabledAlarm == true) R.drawable.ic_toggle_on else R.drawable.ic_toggle_off
    val alpha = if (isEnabledAlarm == null) 0f else 1f

    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "퀘스트 오픈 알림",
            style = ByeBooTheme.typography.body3,
            color = ByeBooTheme.colors.gray50,
        )

        Spacer(modifier = Modifier.weight(1f))

        Image(
            imageVector = ImageVector.vectorResource(toggle),
            contentDescription = "alarm toggle",
            alpha = alpha,
            modifier = Modifier
                .noRippleClickable(
                    onClick = {
                        isEnabledAlarm?.let { onCheckedClick(!isEnabledAlarm) }
                    }
                )
        )
    }
}
