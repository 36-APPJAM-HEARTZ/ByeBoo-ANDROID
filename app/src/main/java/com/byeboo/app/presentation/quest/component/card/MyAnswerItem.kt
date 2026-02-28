package com.byeboo.app.presentation.quest.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.noRippleClickable
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.quest.model.MyAnswerModel

@Composable
fun MyAnswerItem(
    answer: MyAnswerModel,
    modifier: Modifier = Modifier,
    onMyAnswerContentClick: (Long) -> Unit = {},
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(12.dp))
                .background(
                    color = ByeBooTheme.colors.whiteAlpha5,
                ).noRippleClickable {
                    onMyAnswerContentClick(answer.answerId)
                }.padding(
                    horizontal = screenWidthDp(24.dp),
                    vertical = screenHeightDp(16.dp),
                ),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(screenWidthDp(4.dp)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Q.",
                color = ByeBooTheme.colors.primary200,
                style = ByeBooTheme.typography.sub2,
            )

            Text(
                text = answer.question,
                color = ByeBooTheme.colors.gray100,
                style = ByeBooTheme.typography.sub3,
            )
        }

        Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))

        Text(
            text = answer.content,
            color = ByeBooTheme.colors.gray100,
            style = ByeBooTheme.typography.body3,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))

        Text(
            text = answer.writtenAt,
            color = ByeBooTheme.colors.gray400,
            style = ByeBooTheme.typography.cap2,
        )
    }
}
