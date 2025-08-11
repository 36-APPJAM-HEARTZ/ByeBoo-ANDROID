package com.byeboo.app.presentation.mypage.component

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.byeboo.app.core.designsystem.component.button.ByeBooButton
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenWidthDp

@Composable
fun MyPageModal(
    onDismissRequest: () -> Unit,
    myPageModalMainText: String,
    onCancelClick: () -> Unit,
    onConfirmClick: () -> Unit,
    onConfirmText: String,
    modifier: Modifier = Modifier,
    dialogProperties: DialogProperties = DialogProperties(),
    myPageModalSubText: String? = null,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = dialogProperties
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(12.dp))
                .background(color = ByeBooTheme.colors.gray900Alpha80)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = myPageModalMainText,
                color = ByeBooTheme.colors.gray50,
                style = ByeBooTheme.typography.sub3
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (myPageModalSubText != null) {
                Text(
                    text = myPageModalSubText,
                    color = ByeBooTheme.colors.gray400,
                    style = ByeBooTheme.typography.body3
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                ByeBooButton(
                    onClick = onCancelClick,
                    buttonText = "취소",
                    buttonTextColor = ByeBooTheme.colors.gray200,
                    buttonStrokeColor = ByeBooTheme.colors.gray400,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(screenWidthDp(16.dp)))

                ByeBooButton(
                    onClick = onConfirmClick,
                    buttonText = onConfirmText,
                    buttonTextColor = ByeBooTheme.colors.white,
                    buttonBackgroundColor = ByeBooTheme.colors.primary300,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}