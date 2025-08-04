package com.byeboo.app.presentation.auth.onboarding

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.component.backhandler.ByeBooBackHandler
import com.byeboo.app.core.designsystem.component.button.ByeBooButton
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp

@Composable
fun OnboardingRoute(
    navigateToUserInfo: () -> Unit,
    padding: Dp,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val pageIndex by viewModel.pageIndex

    val contents = viewModel.currentContents()


    if (pageIndex != 0) {
        BackHandler {
            viewModel.previousPage()
        }
    } else {
        ByeBooBackHandler()
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when(effect) {
                is OnboardingSideEffect.NavigationToUserInfo -> navigateToUserInfo()
            }
        }
    }

    OnboardingScreen(
        padding = padding,
        pageIndex = pageIndex,
        contents = contents,
        onSkipPage = viewModel::skipPage,
        onNextPage = viewModel::onNextPage,
        onShowPageNumber = viewModel::showPageNumber,
    )


}

@Composable
private fun OnboardingScreen(
    padding: Dp,
    pageIndex: Int,
    contents: List<OnboardingState>,
    onSkipPage: () -> Unit,
    onNextPage: () -> Unit,
    onShowPageNumber: () -> String,
) {
    val pageSpace = if (pageIndex == 2) 24.dp else 16.dp

    val buttonText = if (pageIndex == 2) "시작하기" else "다음으로"

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.img_onboarding_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier.padding(top = screenHeightDp(padding + 27.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.padding(horizontal = screenWidthDp(24.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = onShowPageNumber(),
                    color = ByeBooTheme.colors.primary300,
                    style = ByeBooTheme.typography.body5
                )

                Spacer(modifier = Modifier.weight(1f))

                if (pageIndex != 2) {
                    Row(
                        modifier = Modifier.clickable { onSkipPage() },
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        Text(
                            text = "SKIP",
                            color = ByeBooTheme.colors.primary300,
                            style = ByeBooTheme.typography.body5.copy(
                                textDecoration = TextDecoration.Underline
                            )
                        )

                        Spacer(modifier = Modifier.width(screenWidthDp(4.dp)))

                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_right),
                            contentDescription = "next",
                            tint = ByeBooTheme.colors.primary300,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = screenWidthDp(45.dp)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                contents.forEach { content ->

                    Image(
                        painter = painterResource(id = content.imageRes),
                        contentDescription = "OnBoarding image",
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.FillWidth
                    )

                    Spacer(modifier = Modifier.height(screenHeightDp(pageSpace)))

                    Text(
                        text = content.title,
                        color = ByeBooTheme.colors.gray900,
                        style = ByeBooTheme.typography.body3,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            ByeBooButton(
                modifier = Modifier
                    .padding(horizontal = screenWidthDp(24.dp))
                    .padding(bottom = padding),
                buttonText = buttonText,
                onClick = onNextPage,
                buttonTextColor = ByeBooTheme.colors.white,
                buttonBackgroundColor = ByeBooTheme.colors.primary300
            )
        }
    }
}
