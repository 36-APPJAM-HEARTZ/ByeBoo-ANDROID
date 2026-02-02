package com.byeboo.app.presentation.auth.userinfo.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.component.text.DescriptionText
import com.byeboo.app.domain.model.auth.Feeling
import com.byeboo.app.presentation.auth.userinfo.component.UserInfoEmotionCard
import kotlinx.collections.immutable.persistentListOf

@Composable
fun UserInfoEmotionScreen(
    selectedEmotion: Feeling?,
    onEmotionSelect: (Feeling) -> Unit,
) {
    val emotions =
        persistentListOf(
            Feeling.EXHAUSTED,
            Feeling.RECOVERING,
            Feeling.OVERCOMING,
        )

    Column {
        DescriptionText(
            title = "감정 상태",
            guideText = "를 알려주세요",
            contentText = "이별 후, 어떤 감정으로 하루를 보내고 계신가요?",
            bottom = 20.dp,
        )
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            emotions.forEach { emotion ->
                val onCardClick =
                    remember(emotion) {
                        { onEmotionSelect(emotion) }
                    }
                UserInfoEmotionCard(
                    content = emotion.displayText,
                    imageRes =
                        when (emotion) {
                            Feeling.EXHAUSTED -> R.drawable.ic_emotion_sad
                            Feeling.RECOVERING -> R.drawable.ic_emotion_soso
                            Feeling.OVERCOMING -> R.drawable.ic_emotion_good
                        },
                    isSelected = selectedEmotion == emotion,
                    onCardClick = onCardClick,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}
