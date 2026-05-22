package com.byeboo.app.presentation.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.noRippleClickable
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.quest.util.iconResId

@Composable
fun NotificationCard(
    notification: NotificationModel,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = if (notification.isRead) ByeBooTheme.colors.whiteAlpha5 else ByeBooTheme.colors.primary300Alpha20,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(
                horizontal = screenWidthDp(24.dp),
                vertical = screenHeightDp(12.dp)
            )
            .noRippleClickable { onClick(notification.landingLink) }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(screenHeightDp(12.dp))
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(screenWidthDp(4.dp))
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        id = notification.iconResId
                    ),
                    contentDescription = null
                )

                Text(
                    text = notification.title,
                    color = ByeBooTheme.colors.gray200,
                    style = ByeBooTheme.typography.body1
                )
            }

            Text(
                text = notification.content,
                color = ByeBooTheme.colors.gray100,
                style = ByeBooTheme.typography.body6
            )

            Spacer(modifier = Modifier.height(screenHeightDp(8.dp)))

            Text(
                text = notification.createdAt,
                color = ByeBooTheme.colors.gray400,
                style = ByeBooTheme.typography.cap2
            )
        }
    }
}