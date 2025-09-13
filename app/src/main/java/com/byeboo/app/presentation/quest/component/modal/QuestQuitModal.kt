package com.byeboo.app.presentation.quest.component.modal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.byeboo.app.core.designsystem.component.button.ByeBooButton
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp

@Composable
fun QuestQuitModal(
    onDismissRequest: () -> Unit,
    stayButton: () -> Unit,
    quitButton: () -> Unit,
    modifier: Modifier = Modifier,
    dialogProperties: DialogProperties = DialogProperties()
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = dialogProperties
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(color = ByeBooTheme.colors.gray800)
                .padding(horizontal = screenWidthDp(24.dp), vertical = screenHeightDp(24.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "작성을 중단하시겠어요?",
                style = ByeBooTheme.typography.sub3,
                color = ByeBooTheme.colors.gray50
            )

            Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

            Text(
                text = "작성하시던 내용은\n저장되지 않아요.",
                style = ByeBooTheme.typography.body3,
                color = ByeBooTheme.colors.gray400,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ByeBooButton(
                    onClick = quitButton,
                    buttonText = "나가기",
                    buttonStyle = ByeBooTheme.typography.body3,
                    buttonTextColor = ByeBooTheme.colors.gray200,
                    buttonStrokeColor = ByeBooTheme.colors.gray400,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(screenWidthDp(16.dp)))

                ByeBooButton(
                    onClick = stayButton,
                    buttonText = "머무르기",
                    buttonStyle = ByeBooTheme.typography.body2,
                    buttonTextColor = ByeBooTheme.colors.white,
                    buttonBackgroundColor = ByeBooTheme.colors.primary300,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
