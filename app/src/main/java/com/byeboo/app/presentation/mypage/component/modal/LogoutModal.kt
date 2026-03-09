package com.byeboo.app.presentation.mypage.component.modal

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import com.byeboo.app.core.designsystem.component.modal.ByeBooModal

@Composable
fun LogoutModal(
    onDismissRequest: () -> Unit,
    onCancelClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier,
    dialogProperties: DialogProperties = DialogProperties(usePlatformDefaultWidth = false),
) {
    ByeBooModal(
        onDismissRequest = onDismissRequest,
        modalMainText = "로그아웃하시겠어요?",
        onLeftButtonClick = onCancelClick,
        onLeftButtonText = "취소",
        onRightButtonClick = onLogoutClick,
        onRightButtonText = "로그아웃",
        dialogProperties = dialogProperties,
        modifier = modifier,
    )
}
