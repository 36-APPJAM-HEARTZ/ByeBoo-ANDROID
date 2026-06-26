package com.byeboo.app.presentation.quest.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.noRippleClickable
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.quest.component.button.ReactionCount
import com.byeboo.app.presentation.quest.model.CommonReplyModel

@Composable
fun CommonReplyItem(
    reply: CommonReplyModel,
    onMoreOptionsClick: () -> Unit,
    modifier: Modifier = Modifier,
    onCommentClick: () -> Unit = {},
    isReply: Boolean = false,
    onClick: () -> Unit = {},
) {
    var isExpanded by remember { mutableStateOf(false) }
    var measuredLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    val gray400Color = ByeBooTheme.colors.gray400
    val body6FontSize = ByeBooTheme.typography.body6.fontSize

    val displayText =
        remember(reply.content, isExpanded, measuredLayoutResult) {
            buildAnnotatedString {
                val layout = measuredLayoutResult
                if (isExpanded || layout == null || layout.lineCount <= 5) {
                    append(reply.content)
                } else {
                    val lastCharIndex = layout.getLineEnd(4)
                    val adjustedIndex = (lastCharIndex - 5).coerceAtLeast(0)

                    append(reply.content.substring(0, adjustedIndex))
                    append("... ")

                    withLink(
                        LinkAnnotation.Clickable(
                            tag = "EXPAND",
                            styles =
                                TextLinkStyles(
                                    style =
                                        SpanStyle(
                                            color = gray400Color,
                                            fontSize = body6FontSize,
                                        ),
                                ),
                            linkInteractionListener = { isExpanded = true },
                        ),
                    ) {
                        append("더보기")
                    }
                }
            }
        }

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .background(color = ByeBooTheme.colors.background)
                .noRippleClickable(onClick = onClick)
                .padding(vertical = screenHeightDp(16.dp)),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        start = if (isReply) screenWidthDp(24.dp) else 0.dp,
                        end = screenWidthDp(24.dp),
                    ),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(screenWidthDp(4.dp)),
            ) {
                Icon(
                    painter = painterResource(id = reply.profileIconRes),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(20.dp),
                )

                Text(
                    text = reply.writer,
                    style = ByeBooTheme.typography.body6,
                    color = ByeBooTheme.colors.gray200,
                )

                Text(
                    text = reply.displayTime,
                    style = ByeBooTheme.typography.cap2,
                    color = ByeBooTheme.colors.gray400,
                )
            }

            Spacer(modifier = Modifier.height(screenHeightDp(4.dp)))

            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(start = screenWidthDp(20.dp)),
            ) {
                Text(
                    text = reply.content,
                    style = ByeBooTheme.typography.body6,
                    color = Color.Transparent,
                    maxLines = 5,
                    modifier = Modifier.fillMaxWidth(),
                    onTextLayout = { result ->
                        if (measuredLayoutResult == null) measuredLayoutResult = result
                    },
                )

                Text(
                    text = displayText,
                    style = ByeBooTheme.typography.body6.copy(color = ByeBooTheme.colors.gray100),
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Spacer(modifier = Modifier.height(screenHeightDp(8.dp)))

            if (!isReply) {
                Row(
                    modifier = Modifier.padding(start = screenWidthDp(20.dp)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(screenWidthDp(4.dp)),
                ) {
                    ReactionCount(
                        iconImg = R.drawable.ic_comment,
                        count = reply.replyCount,
                        modifier = Modifier.noRippleClickable { onCommentClick() },
                    )
                }
            }
        }

        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_overflow_menu),
            contentDescription = null,
            tint = ByeBooTheme.colors.white,
            modifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .noRippleClickable(onClick = onMoreOptionsClick),
        )

        if (isReply) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_reply),
                contentDescription = null,
                tint = ByeBooTheme.colors.gray400,
                modifier = Modifier.align(Alignment.TopStart),
            )
        }
    }
}
