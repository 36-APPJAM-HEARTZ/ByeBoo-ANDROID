package com.byeboo.app.presentation.quest.component.modal

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import com.byeboo.app.core.designsystem.component.modal.ByeBooModal

@Composable
fun QuestCompleteModal(
    onDismissRequest: () -> Unit,
    onNoClick: () -> Unit,
    onYesClick: () -> Unit,
    modifier: Modifier = Modifier,
    dialogProperties: DialogProperties = DialogProperties(usePlatformDefaultWidth = false),
) {
    ByeBooModal(
        onDismissRequest = onDismissRequest,
        modalMainText = "작성을 완료하시겠어요?",
        onLeftButtonClick = onNoClick,
        onLeftButtonText = "아니오",
        onRightButtonClick = onYesClick,
        onRightButtonText = "예",
        modalSubText = "완료하면 다른 사용자에게 공개돼요.",
        dialogProperties = dialogProperties,
        modifier = modifier,
    )
}
