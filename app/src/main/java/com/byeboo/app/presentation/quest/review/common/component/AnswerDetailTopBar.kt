package com.byeboo.app.presentation.quest.review.common.component

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.component.topbar.ByeBooTopbar
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.noRippleClickable

@Composable
fun AnswerDetailTopBar(
    onBackClick: () -> Unit,
    onMoreOptionsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ByeBooTopbar(
        navigationIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_left),
                contentDescription = null,
                tint = ByeBooTheme.colors.gray50,
                modifier =
                    Modifier.noRippleClickable(
                        onClick = onBackClick,
                    ),
            )
        },
        actions = {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_overflow_menu),
                contentDescription = null,
                tint = ByeBooTheme.colors.white,
                modifier =
                    Modifier
                        .noRippleClickable(
                            onClick = onMoreOptionsClick,
                        ),
            )
        },
        modifier = modifier,
    )
}
