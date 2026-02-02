package com.byeboo.app.presentation.quest.component.text

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SmallTag(
            tagText = "STEP $stepNumber",
        )

        Spacer(modifier = Modifier.width(screenWidthDp(8.dp)))

        Text(
            text = stepTitle,
            color = ByeBooTheme.colors.gray50,
            style = ByeBooTheme.typography.body2,
        )
    }
}
