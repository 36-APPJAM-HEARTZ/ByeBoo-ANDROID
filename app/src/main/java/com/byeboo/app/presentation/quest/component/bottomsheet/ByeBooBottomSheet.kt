package com.byeboo.app.presentation.quest.component.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.component.button.ByeBooActivationButton
import com.byeboo.app.core.designsystem.type.EmotionChipType
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.quest.component.chip.EmotionChip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ByeBooBottomSheet(
    selectedEmotion: EmotionChipType?,
    navigateButton: () -> Unit,
    onDismiss: () -> Unit,
    onEmotionSelected: (EmotionChipType?) -> Unit,
    modifier: Modifier = Modifier,
    showBottomSheet: Boolean = false,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    isBackgroundDimmed: Boolean = true,
    dragHandle: @Composable () -> Unit = {},
    isUploading: Boolean = false,
) {
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            modifier = modifier,
            sheetState = sheetState,
            containerColor = ByeBooTheme.colors.gray900,
            scrimColor =
                if (isBackgroundDimmed) {
                    ByeBooTheme.colors.blackAlpha80
                } else {
                    Color.Transparent
                },
            dragHandle = dragHandle,
        ) {
            LaunchedEffect(showBottomSheet) {
                if (showBottomSheet) {
                    onEmotionSelected(null)
                }
            }

            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = screenWidthDp(24.dp))
                        .padding(bottom = screenHeightDp(10.dp)),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ByeBooDragHandle()

                Spacer(modifier = Modifier.height(screenHeightDp(17.dp)))

                Text(
                    text = "퀘스트를 완료한 후,\n어떤 감정이 느껴지시나요?",
                    color = ByeBooTheme.colors.gray50,
                    textAlign = TextAlign.Center,
                    style = ByeBooTheme.typography.head2,
                )

                Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(screenWidthDp(3.dp)),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_caution),
                        contentDescription = "caution icon",
                        modifier =
                            Modifier
                                .size(16.dp),
                        tint = Color.Unspecified,
                    )

                    Text(
                        text = "퀘스트 완료 후에는 감정을 수정할 수 없어요",
                        color = ByeBooTheme.colors.gray400,
                        style = ByeBooTheme.typography.cap2,
                    )
                }

                Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

                EmotionChipList(
                    selectedEmotion = selectedEmotion,
                    onEmotionSelected = {
                        val newEmotion = if (selectedEmotion == it) null else it
                        onEmotionSelected(newEmotion)
                    },
                    isUploading = isUploading,
                )

                Spacer(modifier = Modifier.height(screenHeightDp(37.dp)))

                ByeBooActivationButton(
                    buttonDisableColor = ByeBooTheme.colors.whiteAlpha5,
                    buttonText = "완료하기",
                    buttonDisableTextColor = ByeBooTheme.colors.gray300,
                    onClick = {
                        selectedEmotion?.let { emotion ->
                            onEmotionSelected(emotion)
                            navigateButton()
                        }
                    },
                    isEnabled = (selectedEmotion != null) && !isUploading,
                )
            }
        }
    }
}

@Composable
private fun EmotionChipList(
    selectedEmotion: EmotionChipType?,
    onEmotionSelected: (EmotionChipType) -> Unit,
    isUploading: Boolean,
    modifier: Modifier = Modifier,
) {
    val isOthersBackgroundDimmed = selectedEmotion != null

    Column(modifier = modifier.padding(horizontal = screenWidthDp(62.dp))) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            EmotionChip(
                emotionType = EmotionChipType.EMOTION_NEUTRAL,
                isSelected = selectedEmotion == EmotionChipType.EMOTION_NEUTRAL,
                enabled = !isUploading,
                isDimmed = isOthersBackgroundDimmed && selectedEmotion != EmotionChipType.EMOTION_NEUTRAL,
                onChipClick = { onEmotionSelected(EmotionChipType.EMOTION_NEUTRAL) },
            )

            Spacer(modifier = Modifier.width(screenWidthDp(20.dp)))

            EmotionChip(
                emotionType = EmotionChipType.EMOTION_SELF_AWARE,
                isSelected = selectedEmotion == EmotionChipType.EMOTION_SELF_AWARE,
                enabled = !isUploading,
                isDimmed = isOthersBackgroundDimmed && selectedEmotion != EmotionChipType.EMOTION_SELF_AWARE,
                onChipClick = { onEmotionSelected(EmotionChipType.EMOTION_SELF_AWARE) },
            )
        }

        Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            EmotionChip(
                emotionType = EmotionChipType.EMOTION_SADNESS,
                isSelected = selectedEmotion == EmotionChipType.EMOTION_SADNESS,
                enabled = !isUploading,
                isDimmed = isOthersBackgroundDimmed && selectedEmotion != EmotionChipType.EMOTION_SADNESS,
                onChipClick = { onEmotionSelected(EmotionChipType.EMOTION_SADNESS) },
            )

            Spacer(modifier = Modifier.width(screenWidthDp(20.dp)))

            EmotionChip(
                emotionType = EmotionChipType.EMOTION_RELIEF,
                isSelected = selectedEmotion == EmotionChipType.EMOTION_RELIEF,
                enabled = !isUploading,
                isDimmed = isOthersBackgroundDimmed && selectedEmotion != EmotionChipType.EMOTION_RELIEF,
                onChipClick = { onEmotionSelected(EmotionChipType.EMOTION_RELIEF) },
            )
        }
    }
}
