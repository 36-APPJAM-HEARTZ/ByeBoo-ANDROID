package com.byeboo.app.presentation.offboarding.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp

@Composable
fun OffboardingModal(
    journeyName: String,
    nickname: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    dialogProperties: DialogProperties = DialogProperties()
) {
    Dialog(
        onDismissRequest = onClick,
        properties = dialogProperties
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(color = ByeBooTheme.colors.gray900Alpha80)
                .padding(horizontal = screenWidthDp(24.dp), vertical = screenHeightDp(24.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "축하드려요!",
                style = ByeBooTheme.typography.body3,
                color = ByeBooTheme.colors.gray400
            )

            Spacer(modifier = Modifier.height(screenHeightDp(8.dp)))

            Text(
                text = "${journeyName}을\n모두 마무리했어요",
                style = ByeBooTheme.typography.sub3,
                color = ByeBooTheme.colors.gray50,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

            Image(
                painter = painterResource(id = R.drawable.bori_clover),
                contentDescription = "이미지",
                modifier = Modifier.size(160.dp)
            )

            Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

            Text(
                text = "보리가 ${nickname}님께\n하고 싶은 말이 있다고 해요",
                style = ByeBooTheme.typography.body2,
                color = ByeBooTheme.colors.gray400,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

            ByeBooButton(
                onClick = onClick,
                buttonText = "바로가기",
                buttonStyle = ByeBooTheme.typography.body3,
                buttonTextColor = ByeBooTheme.colors.white,
                buttonBackgroundColor = ByeBooTheme.colors.primary300
            )
        }
    }
}
