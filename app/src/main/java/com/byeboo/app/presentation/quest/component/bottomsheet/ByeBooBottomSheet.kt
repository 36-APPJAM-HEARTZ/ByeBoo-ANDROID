package com.byeboo.app.presentation.quest.component.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.byeboo.app.core.designsystem.component.button.ByeBooActivationButton
import com.byeboo.app.core.designsystem.type.LargeTagType
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.quest.component.chip.EmotionChip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ByeBooBottomSheet(
    navigateButton: () -> Unit,
    onDismiss: () -> Unit,
    onEmotionSelected: (LargeTagType) -> Unit,
    onSelectedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    showBottomSheet: Boolean = false,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    isBackgroundDimmed: Boolean = true,
    dragHandle: @Composable () -> Unit = {},
    isSelected: Boolean = false,
    isUploading: Boolean = false
) {
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            modifier = modifier,
            sheetState = sheetState,
            containerColor = ByeBooTheme.colors.gray800,
            scrimColor = if (isBackgroundDimmed) {
                ByeBooTheme.colors.blackAlpha80
            } else {
                Color.Transparent
            },
            dragHandle = dragHandle
        ) {
            var selectedEmotion by remember { mutableStateOf<LargeTagType?>(null) }

            LaunchedEffect(showBottomSheet) {
                if (showBottomSheet) {
                    selectedEmotion = null
                    onSelectedChanged(false)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = screenWidthDp(24.dp), vertical = screenHeightDp(8.dp)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ByeBooDragHandle()

                Text(
                    text = "퀘스트를 완료한 후,\n어떤 감정이 느껴지시나요?",
                    color = ByeBooTheme.colors.gray50,
                    textAlign = TextAlign.Center,
                    style = ByeBooTheme.typography.head1
                )

                Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))

                EmotionChipList(
                    selectedEmotion = selectedEmotion,
                    onEmotionSelected = {
                        val newEmotion = if (selectedEmotion == it) null else it
                        selectedEmotion = newEmotion
                        onSelectedChanged(newEmotion != null)
                    },
                    isUploading = isUploading
                )

                Spacer(modifier = Modifier.height(screenHeightDp(37.dp)))

                ByeBooActivationButton(
                    buttonDisableColor = ByeBooTheme.colors.whiteAlpha10,
                    buttonText = "완료",
                    buttonDisableTextColor = ByeBooTheme.colors.gray300,
                    onClick = {
                        selectedEmotion?.let { emotion ->
                            onEmotionSelected(emotion)
                            navigateButton()
                        }
                    },
                    isEnabled = (selectedEmotion != null) && !isUploading
                )
            }
        }
    }
}

@Composable
private fun EmotionChipList(
    selectedEmotion: LargeTagType?,
    onEmotionSelected: (LargeTagType) -> Unit,
    isUploading: Boolean,
    modifier: Modifier = Modifier
) {
    val dimOthers = selectedEmotion != null

    Column(modifier = modifier.padding(horizontal = screenWidthDp(62.dp))) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            EmotionChip(
                emotionType = LargeTagType.EMOTION_NEUTRAL,
                isSelected = selectedEmotion == LargeTagType.EMOTION_NEUTRAL,
                enabled = !isUploading,
                isDimmed = dimOthers && selectedEmotion != LargeTagType.EMOTION_NEUTRAL,
                onChipClick = { onEmotionSelected(LargeTagType.EMOTION_NEUTRAL) }
            )

            Spacer(modifier = Modifier.width(screenWidthDp(20.dp)))

            EmotionChip(
                emotionType = LargeTagType.EMOTION_SELF_AWARE,
                isSelected = selectedEmotion == LargeTagType.EMOTION_SELF_AWARE,
                enabled = !isUploading,
                isDimmed = dimOthers && selectedEmotion != LargeTagType.EMOTION_SELF_AWARE,
                onChipClick = { onEmotionSelected(LargeTagType.EMOTION_SELF_AWARE) }
            )
        }

        Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            EmotionChip(
                emotionType = LargeTagType.EMOTION_SADNESS,
                isSelected = selectedEmotion == LargeTagType.EMOTION_SADNESS,
                enabled = !isUploading,
                isDimmed = dimOthers && selectedEmotion != LargeTagType.EMOTION_SADNESS,
                onChipClick = { onEmotionSelected(LargeTagType.EMOTION_SADNESS) }
            )

            Spacer(modifier = Modifier.width(screenWidthDp(20.dp)))

            EmotionChip(
                emotionType = LargeTagType.EMOTION_RELIEF,
                isSelected = selectedEmotion == LargeTagType.EMOTION_RELIEF,
                enabled = !isUploading,
                isDimmed = dimOthers && selectedEmotion != LargeTagType.EMOTION_RELIEF,
                onChipClick = { onEmotionSelected(LargeTagType.EMOTION_RELIEF) }
            )
        }
    }
}
