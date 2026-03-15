package com.byeboo.app.presentation.quest.aianswer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.byeboo.app.core.designsystem.component.topbar.CloseTopbar
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.quest.aianswer.type.QuestAiAnswerStatusType

@Composable
fun QuestAiAnswerStatusScreen(
    paddingValues: PaddingValues,
    onCloseClick: () -> Unit,
    statusType: QuestAiAnswerStatusType,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(color = ByeBooTheme.colors.background)
                .padding(
                    top = paddingValues.calculateTopPadding() + screenHeightDp(43.dp),
                    bottom = paddingValues.calculateBottomPadding(),
                ),
        verticalArrangement = Arrangement.spacedBy(screenHeightDp(227.dp)),
    ) {
        CloseTopbar(
            onCloseClick = onCloseClick,
            modifier = Modifier.padding(horizontal = screenWidthDp(24.dp)),
        )

        QuestAiAnswerStatusContent(
            statusType = statusType,
        )
    }
}

@Composable
private fun QuestAiAnswerStatusContent(statusType: QuestAiAnswerStatusType) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(screenHeightDp(8.dp)),
    ) {
        Image(
            painter = painterResource(id = statusType.statusImage),
            contentDescription = null,
            modifier = Modifier.size(100.dp),
        )

        Text(
            text = statusType.statusContent,
            color = ByeBooTheme.colors.gray100,
            style = ByeBooTheme.typography.body3,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
