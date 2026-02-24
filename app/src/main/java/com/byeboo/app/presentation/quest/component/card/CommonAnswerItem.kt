package com.byeboo.app.presentation.quest.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.noRippleClickable
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.quest.model.CommonAnswerModel

@Composable
fun CommonAnswerItem(
    answer: CommonAnswerModel,
    modifier: Modifier = Modifier,
    isExpanded: Boolean = false,
    onClick: () -> Unit = {}
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .noRippleClickable(
                    onClick = onClick
                )
                .background(
                    color = ByeBooTheme.colors.whiteAlpha5,
                    shape = RoundedCornerShape(12.dp),
                ).padding(
                    horizontal = screenWidthDp(24.dp),
                    vertical = screenHeightDp(16.dp),
                ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(screenWidthDp(4.dp)),
        ) {
            Icon(
                painter = painterResource(id = answer.profileIconRes),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(20.dp),
            )

            Text(
                text = answer.writer,
                style = ByeBooTheme.typography.body6,
                color = ByeBooTheme.colors.gray200,
            )
        }

        Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))

        Text(
            text = answer.content,
            style = ByeBooTheme.typography.body3,
            color = ByeBooTheme.colors.gray100,
            maxLines = if (isExpanded) Int.MAX_VALUE else 2,
            overflow = if (isExpanded) TextOverflow.Visible else TextOverflow.Ellipsis,
        )

        Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))

        Text(
            text = answer.displayTime,
            style = ByeBooTheme.typography.cap2,
            color = ByeBooTheme.colors.gray400,
        )
    }
}
