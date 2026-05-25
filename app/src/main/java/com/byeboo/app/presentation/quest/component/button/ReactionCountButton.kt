package com.byeboo.app.presentation.quest.component.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.noRippleClickable
import com.byeboo.app.core.util.screenWidthDp

@Composable
fun ReactionCountButton(
    isLiked: Boolean,
    heartCount: Int,
    onHeartClick: () -> Unit,
    commentCount: Int,
    modifier: Modifier = Modifier,
    onCommentClick: (Long) -> Unit = {},
    answerId: Long = 0L,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(screenWidthDp(16.dp)),
    ) {
        ReactionCount(
            iconImg = if (isLiked) R.drawable.ic_purple_heart else R.drawable.ic_white_heart,
            count = heartCount,
            modifier =
                Modifier.noRippleClickable(
                    onClick = onHeartClick,
                ),
        )

        ReactionCount(
            iconImg = R.drawable.ic_comment,
            count = commentCount,
            modifier =
                Modifier.noRippleClickable {
                    onCommentClick(answerId)
                },
        )
    }
}

@Composable
private fun ReactionCount(
    @DrawableRes iconImg: Int,
    count: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(screenWidthDp(4.dp)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = iconImg),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
        )

        Text(
            text = "$count",
            style = ByeBooTheme.typography.cap2,
            color = ByeBooTheme.colors.gray100,
        )
    }
}
