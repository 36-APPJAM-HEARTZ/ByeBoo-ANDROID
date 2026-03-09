package com.byeboo.app.presentation.quest.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp

@Composable
fun QuestCompleteDialog(modifier: Modifier = Modifier) {
    Dialog(
        onDismissRequest = {},
        properties =
            DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
            ),
    ) {
        QuestCompleteCard(modifier = modifier)
    }
}

@Composable
private fun QuestCompleteCard(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.bori_congrats))
    val progress by animateLottieCompositionAsState(composition = composition)

    Column(
        modifier =
            modifier
                .background(
                    color = ByeBooTheme.colors.background,
                    shape = RoundedCornerShape(12.dp),
                ).padding(
                    horizontal = screenWidthDp(60.dp),
                    vertical = screenHeightDp(24.dp),
                ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "QUEST",
            color = ByeBooTheme.colors.primary300,
            textAlign = TextAlign.Center,
            style = ByeBooTheme.typography.head1,
        )

        Text(
            text = "COMPLETE!",
            color = ByeBooTheme.colors.primary100,
            textAlign = TextAlign.Center,
            style = ByeBooTheme.typography.head1,
        )

        Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

        LottieAnimation(
            composition = composition,
            progress = progress,
            modifier = Modifier.height(screenHeightDp(172.dp)),
        )

        Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

        Text(
            text = "기특해요!\n점점 극복해 나가고 있어요:)",
            color = ByeBooTheme.colors.gray300,
            textAlign = TextAlign.Center,
            style = ByeBooTheme.typography.body3,
        )
    }
}
