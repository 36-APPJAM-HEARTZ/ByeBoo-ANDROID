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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.component.button.ByeBooButton
import com.byeboo.app.core.designsystem.component.tag.MiddleTag
import com.byeboo.app.core.designsystem.type.MiddleTagType
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
    dialogProperties: DialogProperties = DialogProperties(usePlatformDefaultWidth = false),
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = dialogProperties,
    ) {
        Column(
            modifier =
                modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(color = ByeBooTheme.colors.background)
                    .padding(horizontal = screenWidthDp(24.dp), vertical = screenHeightDp(24.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(screenHeightDp(24.dp)),
        ) {
            QuestModalContent(
                questNumber = questNumber,
                questQuestion = questQuestion,
                navigateToTip = navigateToTip,
            )

            ByeBooButton(
                onClick = progressButton,
                buttonText = "진행하기",
                buttonStyle = ByeBooTheme.typography.body3,
                buttonTextColor = ByeBooTheme.colors.white,
                buttonBackgroundColor = ByeBooTheme.colors.primary300,
            )
        }
    }
}

@Composable
private fun QuestModalContent(
    questNumber: Long,
    questQuestion: String,
    navigateToTip: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = screenWidthDp(8.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_bori_quest_banner),
            contentDescription = null,
            Modifier
                .fillMaxWidth()
                .height(screenHeightDp(62.dp)),
        )

        Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))

        Text(
            text = "${questNumber}번째 퀘스트",
            style = ByeBooTheme.typography.body3,
            color = ByeBooTheme.colors.gray400,
        )

        Spacer(modifier = Modifier.height(screenHeightDp(8.dp)))

        Text(
            text = questQuestion,
            style = ByeBooTheme.typography.sub3,
            color = ByeBooTheme.colors.gray50,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))

        MiddleTag(
            middleTagType = MiddleTagType.QUEST_TIP,
            text = "작성 TIP",
            textStyle = ByeBooTheme.typography.cap1,
            modifier = Modifier.clickable(onClick = navigateToTip),
        )
    }
}
