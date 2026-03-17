package com.byeboo.app.presentation.splash.component

import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.byeboo.app.core.designsystem.component.button.ByeBooButton
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.navigateToPlayStore
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp

@Composable
fun ForceUpdateDialog() {
    val context = LocalContext.current

    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(ByeBooTheme.colors.background)
                .padding(vertical = screenHeightDp(24.dp),horizontal = screenWidthDp(24.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "새로운 업데이트가 있어요",
                style = ByeBooTheme.typography.sub3,
                color = ByeBooTheme.colors.gray50,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

            Text(
                text = "더 나은 바이부를 만나보세요",
                style = ByeBooTheme.typography.body3,
                color = ByeBooTheme.colors.gray400,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

            ByeBooButton(
                onClick = context::navigateToPlayStore,
                buttonText = "업데이트 하러 가기",
                buttonStyle = ByeBooTheme.typography.body2,
                buttonTextColor = ByeBooTheme.colors.white,
                buttonBackgroundColor = ByeBooTheme.colors.primary300
            )
        }
    }
}