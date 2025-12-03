package com.byeboo.app.presentation.splash.termsofservice.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.noRippleClickable
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp

@Composable
fun TermsAllButton(
    onTermsAllClick: () -> Unit,
    modifier: Modifier = Modifier,
    isChecked: Boolean = false
) {
    val checkedIcon = if (isChecked) R.drawable.ic_terms_checked else R.drawable.ic_terms_unchecked
    val textColor = if (isChecked) ByeBooTheme.colors.gray50 else ByeBooTheme.colors.gray300
    val backgroundColor = if (isChecked) ByeBooTheme.colors.primary300Alpha20 else ByeBooTheme.colors.whiteAlpha10

    Column(
        modifier = modifier.padding(vertical = screenHeightDp(8.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(12.dp)
                )
                .then(
                    if (isChecked) {
                        Modifier.border(
                            width = 1.dp,
                            color = ByeBooTheme.colors.primary300,
                            shape = RoundedCornerShape(12.dp)
                        )
                    } else {
                        Modifier
                    }
                )
                .padding(horizontal = screenWidthDp(24.dp), vertical = screenHeightDp(18.dp))
                .noRippleClickable(onClick = onTermsAllClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(checkedIcon),
                contentDescription = "all agree checkButton",
                tint = Color.Unspecified
            )

            Spacer(modifier = modifier.width(screenWidthDp(8.dp)))

            Text(
                text = "전체 동의",
                style = ByeBooTheme.typography.body3,
                color = textColor
            )
        }
    }
}
