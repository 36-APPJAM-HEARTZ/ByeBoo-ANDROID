package com.byeboo.app.presentation.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import kotlinx.coroutines.delay

@Composable
fun SpeechBubbleWithText(firstText: String, secondText: String, thirdText: String) {
    var showFirstText by remember { mutableStateOf(false) }
    var showSecondText by remember { mutableStateOf(false) }
    var showThirdText by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        showFirstText = true
        delay(1000)
        showFirstText = false

        showSecondText = true
        delay(1000)
        showSecondText = false

        showThirdText = true
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(),
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_speech_bubble),
            contentDescription = "말풍선",
            modifier = Modifier.fillMaxWidth()
        )
        val textToShow = when {
            showFirstText -> firstText
            showSecondText -> secondText
            showThirdText -> thirdText
            else -> null
        }
        if (textToShow != null) {
            Text(
                text = textToShow,
                style = ByeBooTheme.typography.body2,
                color = ByeBooTheme.colors.primary50,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 13.5.dp)
                    .padding(top = 13.2.dp)
            )
        }
    }
}