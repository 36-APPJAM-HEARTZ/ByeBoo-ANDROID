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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.byeboo.app.core.designsystem.component.tag.SmallTag
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp

@Composable
fun QuestCompleteTitle(
    stepNumber: Long,
    questNumber: Long,
    createdAt: String,
    questQuestion: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = screenHeightDp(10.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            SmallTag(tagText = "STEP $stepNumber")

            Spacer(modifier = Modifier.width(screenWidthDp(8.dp)))

            Text(
                text = "${questNumber}번째 퀘스트",
                color = ByeBooTheme.colors.gray400,
                style = ByeBooTheme.typography.body5,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))

        Text(
            text = createdAt,
            color = ByeBooTheme.colors.gray400,
            style = ByeBooTheme.typography.body5,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))

        Text(
            text = questQuestion,
            color = ByeBooTheme.colors.gray100,
            style = ByeBooTheme.typography.head1,
            textAlign = TextAlign.Center
        )
    }
}
