package com.byeboo.app.presentation.quest.component.text

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.byeboo.app.core.designsystem.component.tag.MiddleTag
import com.byeboo.app.core.designsystem.type.MiddleTagType
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenHeightDp

@Composable
fun QuestWritingTitle(
    questNumber: Long? = null,
    question: String,
    onTipClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val questTitle = if (questNumber == null) "공통퀘스트" else "${questNumber}번째 퀘스트"

    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = questTitle,
            modifier = Modifier.fillMaxWidth(),
            color = ByeBooTheme.colors.gray500,
            textAlign = TextAlign.Center,
            style = ByeBooTheme.typography.body6,
        )

        Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))

        Text(
            text = question,
            modifier = Modifier.fillMaxWidth(),
            color = ByeBooTheme.colors.gray100,
            textAlign = TextAlign.Center,
            style = ByeBooTheme.typography.head1,
        )

        Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

        if (onTipClick != null) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                MiddleTag(
                    middleTagType = MiddleTagType.QUEST_TIP,
                    text = "작성 TIP",
                    textStyle = ByeBooTheme.typography.cap1,
                    modifier = Modifier.clickable(onClick = onTipClick),
                )
            }
        }
    }
}
