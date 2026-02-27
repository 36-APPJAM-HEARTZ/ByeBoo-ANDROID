package com.byeboo.app.presentation.quest.component.text

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenWidthDp

@Composable
fun QuestWritingFooter(
    currentCharCount: Int,
    isPhotoQuestion: Boolean,
    modifier: Modifier = Modifier,
) {
    val maxCharCount = if (isPhotoQuestion) 200 else 500

    Row(
        modifier =
            modifier
                .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (!isPhotoQuestion) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_caution),
                contentDescription = null,
                tint = Color.Unspecified,
            )

            Spacer(modifier = Modifier.width(screenWidthDp(3.dp)))

            Text(
                text = "10글자 이상 작성해 주세요.",
                style = ByeBooTheme.typography.cap2,
                color = ByeBooTheme.colors.gray400,
                textAlign = TextAlign.Start,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text =
                buildAnnotatedString {
                    append(text = currentCharCount.toString())

                    append(text = "/")

                    append(text = maxCharCount.toString())
                },
            style = ByeBooTheme.typography.body6,
            color = ByeBooTheme.colors.gray400,
        )
    }
}
