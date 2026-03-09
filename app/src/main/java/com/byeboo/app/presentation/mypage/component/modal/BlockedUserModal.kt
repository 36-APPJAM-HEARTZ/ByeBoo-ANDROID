package com.byeboo.app.presentation.mypage.component.modal

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import com.byeboo.app.core.designsystem.component.modal.ByeBooModal

@Composable
fun BlockedUserModal(
    onDismissRequest: () -> Unit,
    onNoClick: () -> Unit,
    onYesClick: () -> Unit,
    modifier: Modifier = Modifier,
    dialogProperties: DialogProperties = DialogProperties(usePlatformDefaultWidth = false),
) {
    ByeBooModal(
        onDismissRequest = onDismissRequest,
        modalMainText = "차단을 해제하시겠어요?",
        onLeftButtonClick = onNoClick,
        onLeftButtonText = "아니오",
        onRightButtonClick = onYesClick,
        onRightButtonText = "예",
        dialogProperties = dialogProperties,
        modifier = modifier,
    )
}
