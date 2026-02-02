package com.byeboo.app.presentation.quest.component.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.formatHourMinutes

@Composable
fun CompleteContent(
    questNumber: Long,
    imageResId: Int,
    modifier: Modifier = Modifier
) {
    QuestCardContainer(
        modifier = modifier,
        backgroundColor = ByeBooTheme.colors.whiteAlpha10
    ) {
        BackgroundImageLayer(imageResId)

        QuestNumberLabel(
            questNumber = questNumber,
            color = ByeBooTheme.colors.whiteAlpha50,
            modifier =
            Modifier
                .align(Alignment.TopStart)
        )
    }
}

@Composable
fun AvailableContent(
    questNumber: Long,
    imageResId: Int,
    modifier: Modifier = Modifier
) {
    QuestCardContainer(
        modifier = modifier,
        backgroundColor = ByeBooTheme.colors.primary300Alpha20,
        borderColor = ByeBooTheme.colors.primary300
    ) {
        BackgroundImageLayer(imageResId)

        QuestNumberLabel(
            questNumber = questNumber,
            color = ByeBooTheme.colors.primary300,
            modifier =
            Modifier
                .align(Alignment.TopStart)
        )
    }
}

@Composable
fun TimerLockedContent(
    questNumber: Long,
    remainingTime: Long,
    modifier: Modifier = Modifier
) {
    QuestCardContainer(
        modifier = modifier,
        backgroundColor = ByeBooTheme.colors.whiteAlpha10,
        borderColor = ByeBooTheme.colors.secondary300
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = String.format("%02d", questNumber),
                style = ByeBooTheme.typography.cap1,
                color = ByeBooTheme.colors.secondary300,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_lock),
                contentDescription = "locked",
                tint = ByeBooTheme.colors.whiteAlpha10,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Text(
                text = formatHourMinutes(remainingTime),
                style = ByeBooTheme.typography.cap1,
                color = ByeBooTheme.colors.secondary300,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun LockedContent(
    questNumber: Long,
    modifier: Modifier = Modifier
) {
    QuestCardContainer(
        modifier = modifier,
        backgroundColor = ByeBooTheme.colors.whiteAlpha10
    ) {
        QuestNumberLabel(questNumber, ByeBooTheme.colors.whiteAlpha10)
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_lock),
            contentDescription = "locked",
            tint = ByeBooTheme.colors.whiteAlpha10,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
