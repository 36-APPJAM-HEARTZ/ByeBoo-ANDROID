package com.byeboo.app.presentation.quest.component.text

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.byeboo.app.core.designsystem.component.tag.SmallTag
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun QuestTitle(
    stepNumber: Long,
    questNumber: Long,
    createdAt: String,
    questQuestion: String,
) {
    val date =
        remember(createdAt) {
            LocalDate.parse(createdAt).format(DateTimeFormatter.ofPattern("yyyy. MM. dd."))
        }
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = screenHeightDp(10.dp)),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SmallTag(tagText = "STEP $stepNumber", tagColor = ByeBooTheme.colors.gray500)

            Spacer(modifier = Modifier.width(screenWidthDp(8.dp)))

            Text(
                text = "${questNumber}번째 퀘스트",
                color = ByeBooTheme.colors.gray500,
                style = ByeBooTheme.typography.body6,
            )
        }

        Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))

        Text(
            text = "$date",
            color = ByeBooTheme.colors.gray500,
            style = ByeBooTheme.typography.body6,
        )

        Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))

        Row {
            Text(
                text = "Q.",
                color = ByeBooTheme.colors.primary200,
                style = ByeBooTheme.typography.head2,
                modifier = Modifier.padding(end = 4.dp)
            )

            Text(
                text = questQuestion,
                color = ByeBooTheme.colors.gray100,
                style = ByeBooTheme.typography.head2,
            )
        }
    }
}
