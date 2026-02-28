package com.byeboo.app.core.designsystem.component.snackbar

import androidx.compose.foundation.background
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
import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme

@Composable
fun CustomSnackBar(
    customSnackBarType: CustomSnackBarType,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .background(
                    color = ByeBooTheme.colors.blackAlpha80,
                    shape = RoundedCornerShape(12.dp),
                ).padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = customSnackBarType.icon),
            contentDescription = "알림",
            tint = Color.Unspecified,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = customSnackBarType.message,
            style = ByeBooTheme.typography.body6,
            color = ByeBooTheme.colors.gray50,
        )
    }
}
