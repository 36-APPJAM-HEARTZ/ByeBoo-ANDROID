package com.byeboo.app.presentation.mypage.component.modal

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import com.byeboo.app.core.designsystem.component.modal.ByeBooModal

@Composable
fun DeleteAccountModal(
    onDismissRequest: () -> Unit,
    onCancelClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    modifier: Modifier = Modifier,
    dialogProperties: DialogProperties = DialogProperties(usePlatformDefaultWidth = false),
) {
    ByeBooModal(
        onDismissRequest = onDismissRequest,
        modalMainText = "정말 탈퇴하시겠어요?",
        onLeftButtonClick = onCancelClick,
        onLeftButtonText = "취소",
        onRightButtonClick = onDeleteAccountClick,
        onRightButtonText = "탈퇴하기",
        modalSubText = "탈퇴 시 모든 데이터가 삭제됩니다.",
        dialogProperties = dialogProperties,
        modifier = modifier,
    )
}
