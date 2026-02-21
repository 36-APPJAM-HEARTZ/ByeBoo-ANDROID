package com.byeboo.app.presentation.quest.component.modal

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import com.byeboo.app.core.designsystem.component.modal.ByeBooModal

@Composable
private fun QuestDeleteModal(
    onDismissRequest: () -> Unit,
    onNoClick: () -> Unit,
    onYesClick: () -> Unit,
    modifier: Modifier = Modifier,
    dialogProperties: DialogProperties = DialogProperties(usePlatformDefaultWidth = false),
) {
    ByeBooModal(
        onDismissRequest = onDismissRequest,
        modalMainText = "정말 삭제하시곘어요?",
        onLeftButtonClick = onNoClick,
        onLeftButtonText = "아니오",
        onRightButtonClick = onYesClick,
        onRightButtonText = "예",
        modalSubText = "삭제된 답변은 다시 복구할 수 없습니다.",
        dialogProperties = dialogProperties,
        modifier = modifier,
    )
}
