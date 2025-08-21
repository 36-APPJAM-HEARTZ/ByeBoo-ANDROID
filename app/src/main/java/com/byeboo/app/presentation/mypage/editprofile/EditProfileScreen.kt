package com.byeboo.app.presentation.mypage.editprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme

@Composable
fun EditProfileRoute(
    bottomPadding: Dp,
    modifier: Modifier = Modifier
){
    EditProfileScreen(
        bottomPadding = bottomPadding
    )
}

@Composable
private fun EditProfileScreen(
    bottomPadding: Dp,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = ByeBooTheme.colors.black)
            .padding(horizontal = 24.dp)
            .padding(top = 67.dp, bottom = bottomPadding)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_left),
                contentDescription = "",
                tint = ByeBooTheme.colors.gray50
            )

            Text(
                text = "프로필 수정",
                style = ByeBooTheme.typography.sub1,
                color = ByeBooTheme.colors.white,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "닉네임",
            style = ByeBooTheme.typography.body1,
            color = ByeBooTheme.colors.gray300
        )

        Spacer(modifier = Modifier.height(8.dp))

    }
}