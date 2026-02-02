package com.byeboo.app.presentation.quest.component.modal

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.component.button.ByeBooButton
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp

@Composable
fun QuestModal(
    onDismissRequest: () -> Unit,
    questNumber: Long,
    questQuestion: String,
    navigateToTip: () -> Unit,
    progressButton: () -> Unit,
    modifier: Modifier = Modifier,
    dialogProperties: DialogProperties = DialogProperties()
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = dialogProperties
    ) {
        Column(
            modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(color = ByeBooTheme.colors.gray800)
                .padding(horizontal = screenWidthDp(24.dp), vertical = screenHeightDp(24.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.bori_quest_banner),
                contentDescription = "이미지",
                Modifier
                    .fillMaxWidth()
                    .height(screenHeightDp(58.dp))
            )

            Spacer(modifier = Modifier.height(screenHeightDp((17.5).dp)))

            Text(
                text = "${questNumber}번째 퀘스트",
                style = ByeBooTheme.typography.body3,
                color = ByeBooTheme.colors.gray400
            )

            Spacer(modifier = Modifier.height(screenHeightDp(8.dp)))

            Text(
                text = questQuestion,
                style = ByeBooTheme.typography.sub3,
                color = ByeBooTheme.colors.gray50,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

            Text(
                text =
                buildAnnotatedString {
                    withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                        append(text = "작성 TIP")
                    }
                },
                style = ByeBooTheme.typography.body5,
                color = ByeBooTheme.colors.gray300,
                modifier = Modifier.clickable { navigateToTip() }
            )

            Spacer(modifier = Modifier.height(screenHeightDp((17.5).dp)))

            ByeBooButton(
                onClick = progressButton,
                buttonText = "진행하기",
                buttonStyle = ByeBooTheme.typography.body3,
                buttonTextColor = ByeBooTheme.colors.white,
                buttonBackgroundColor = ByeBooTheme.colors.primary300
            )
        }
    }
}
