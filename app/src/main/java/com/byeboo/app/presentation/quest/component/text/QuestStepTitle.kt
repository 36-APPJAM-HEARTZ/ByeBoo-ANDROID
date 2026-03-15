package com.byeboo.app.presentation.quest.component.text

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.byeboo.app.core.designsystem.component.tag.SmallTag
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenWidthDp

@Composable
fun QuestStepTitle(
    stepNumber: Long,
    stepTitle: String,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(screenWidthDp(8.dp)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SmallTag(
            tagText = "STEP $stepNumber",
        )

        Text(
            text = stepTitle,
            color = ByeBooTheme.colors.gray50,
            style = ByeBooTheme.typography.body2,
        )
    }
}
