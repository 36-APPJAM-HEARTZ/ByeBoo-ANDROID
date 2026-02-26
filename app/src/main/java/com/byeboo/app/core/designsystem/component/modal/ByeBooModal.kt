package com.byeboo.app.core.designsystem.component.modal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
fun ByeBooModal(
    onDismissRequest: () -> Unit,
    modalMainText: String,
    onLeftButtonClick: () -> Unit,
    onLeftButtonText: String,
    onRightButtonClick: () -> Unit,
    onRightButtonText: String,
    modifier: Modifier = Modifier,
    dialogProperties: DialogProperties = DialogProperties(),
    modalSubText: String? = null,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = dialogProperties,
    ) {
        Column(
            modifier =
                modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(12.dp))
                    .background(color = ByeBooTheme.colors.background)
                    .padding(horizontal = screenWidthDp(24.dp), vertical = screenHeightDp(24.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(screenHeightDp(16.dp)),
        ) {
            Text(
                text = modalMainText,
                color = ByeBooTheme.colors.gray50,
                style = ByeBooTheme.typography.sub3,
            )

            if (modalSubText != null) {
                Text(
                    text = modalSubText,
                    color = ByeBooTheme.colors.gray400,
                    style = ByeBooTheme.typography.body3,
                    textAlign = TextAlign.Center,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(screenWidthDp(16.dp)),
            ) {
                ByeBooButton(
                    onClick = onLeftButtonClick,
                    buttonText = onLeftButtonText,
                    buttonStyle = ByeBooTheme.typography.body3,
                    buttonTextColor = ByeBooTheme.colors.gray200,
                    buttonStrokeColor = ByeBooTheme.colors.gray400,
                    modifier = Modifier.weight(1f),
                )

                ByeBooButton(
                    onClick = onRightButtonClick,
                    buttonText = onRightButtonText,
                    buttonStyle = ByeBooTheme.typography.body3,
                    buttonTextColor = ByeBooTheme.colors.white,
                    buttonBackgroundColor = ByeBooTheme.colors.primary300,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}
