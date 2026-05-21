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
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp

@Composable
fun NotificationCard(
    notificationType: NotificationType,
    time: String,
    isRead: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = if (isRead) ByeBooTheme.colors.whiteAlpha5 else ByeBooTheme.colors.primary300Alpha20,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(
                horizontal = screenWidthDp(24.dp),
                vertical = screenHeightDp(12.dp)
            )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(screenHeightDp(12.dp))
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(screenWidthDp(4.dp))
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        id = notificationType.icon
                    ),
                    contentDescription = null
                )

                Text(
                    text = notificationType.title,
                    color = ByeBooTheme.colors.gray200,
                    style = ByeBooTheme.typography.body1
                )
            }

            Text(
                text = notificationType.content,
                color = ByeBooTheme.colors.gray100,
                style = ByeBooTheme.typography.body6
            )

            Spacer(modifier = Modifier.height(screenHeightDp(8.dp)))

            Text(
                text = time,
                color = ByeBooTheme.colors.gray400,
                style = ByeBooTheme.typography.cap2
            )
        }
    }
}