package com.byeboo.app.presentation.quest.component.topbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.component.topbar.ByeBooTopbar
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenWidthDp

@Composable
fun QuestWritingTopbar(
    isEnabled: Boolean,
    onBackClick: () -> Unit,
    onCompleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ByeBooTopbar(
        navigationIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_left),
                contentDescription = null,
                tint = ByeBooTheme.colors.white,
                modifier =
                    Modifier
                        .clickable(onClick = onBackClick),
            )
        },
        actions = {
            Text(
                text = "완료",
                color = if (isEnabled) ByeBooTheme.colors.primary300 else ByeBooTheme.colors.gray600,
                style = ByeBooTheme.typography.body2,
                modifier =
                    Modifier
                        .clickable(
                            enabled = isEnabled,
                            onClick = onCompleteClick,
                        ),
            )
        },
        modifier =
            modifier
                .padding(horizontal = screenWidthDp(22.dp)),
    )
}
