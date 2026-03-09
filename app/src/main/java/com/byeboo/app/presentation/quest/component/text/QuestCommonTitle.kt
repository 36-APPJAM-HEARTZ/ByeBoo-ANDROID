package com.byeboo.app.presentation.quest.component.text

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.DateUtil
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp

@Composable
fun QuestCommonTitle(
    createdAt: String,
    questQuestion: String,
    modifier: Modifier = Modifier,
) {
    val date =
        remember(createdAt) {
            DateUtil.formatToDotDate(createdAt)
        }

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = screenHeightDp(10.dp)),
        verticalArrangement = Arrangement.spacedBy(screenHeightDp(12.dp)),
    ) {
        Text(
            text = "공통퀘스트",
            color = ByeBooTheme.colors.gray500,
            style = ByeBooTheme.typography.body6,
        )

        Text(
            text = date,
            color = ByeBooTheme.colors.gray500,
            style = ByeBooTheme.typography.body6,
        )

        Row {
            Text(
                text = "Q.",
                color = ByeBooTheme.colors.primary200,
                style = ByeBooTheme.typography.head2,
                modifier = Modifier.padding(end = screenWidthDp(4.dp)),
            )

            Text(
                text = questQuestion,
                color = ByeBooTheme.colors.gray50,
                style = ByeBooTheme.typography.head2,
            )
        }
    }
}
