package com.byeboo.app.presentation.quest.review.my.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
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
fun QuestReviewTopbar(
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ByeBooTopbar(
        navigationIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_left),
                contentDescription = null,
                tint = ByeBooTheme.colors.white,
                modifier = Modifier.clickable(onClick = onBackClick),
            )
        },
        actions = {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_edit),
                contentDescription = null,
                tint = ByeBooTheme.colors.white,
                modifier = Modifier.clickable(onClick = onEditClick),
            )
        },
        modifier = modifier.padding(horizontal = screenWidthDp(24.dp)),
    )
}
