package com.byeboo.app.presentation.quest.component.modal

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import com.byeboo.app.core.designsystem.component.modal.ByeBooModal

@Composable
fun QuestQuitModal(
    onDismissRequest: () -> Unit,
    stayButton: () -> Unit,
    quitButton: () -> Unit,
    modifier: Modifier = Modifier,
    dialogProperties: DialogProperties = DialogProperties(usePlatformDefaultWidth = false),
) {
    ByeBooModal(
        onDismissRequest = onDismissRequest,
        modalMainText = "작성을 중단하시겠어요?",
        onLeftButtonClick = quitButton,
        onLeftButtonText = "나가기",
        onRightButtonClick = stayButton,
        onRightButtonText = "머무르기",
        dialogProperties = dialogProperties,
        modalSubText = "작성하시던 내용은 저장되지 않아요.",
        modifier = modifier,
    )
}
