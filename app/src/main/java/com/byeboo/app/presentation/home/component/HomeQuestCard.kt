package com.byeboo.app.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.noRippleClickable
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp

@Composable
fun HomeQuestCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = ByeBooTheme.colors.whiteAlpha5,
    shape: Shape = RoundedCornerShape(12.dp),
    borderColor: Color = ByeBooTheme.colors.primary300,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .background(backgroundColor, shape)
                .border(1.dp, color = borderColor, shape)
                .noRippleClickable { onClick() }
                .padding(horizontal = screenWidthDp(24.dp), vertical = screenHeightDp(16.dp)),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = ByeBooTheme.typography.sub2,
                    color = ByeBooTheme.colors.gray50,
                )

                Spacer(modifier = Modifier.height(screenHeightDp(6.dp)))

                Text(
                    text = subtitle,
                    style = ByeBooTheme.typography.body5,
                    color = ByeBooTheme.colors.gray300,
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.ic_right),
                contentDescription = null,
                tint = ByeBooTheme.colors.gray50,
            )
        }
    }
}
