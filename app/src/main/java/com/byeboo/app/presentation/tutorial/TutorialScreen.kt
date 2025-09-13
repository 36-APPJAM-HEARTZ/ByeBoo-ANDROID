package com.byeboo.app.presentation.tutorial

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme

@Composable
fun TutorialRoute(
    navigateToUp: () -> Unit,
    bottomPadding: Dp,
    modifier: Modifier = Modifier,
    viewModel: TutorialViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                TutorialSideEffect.NavigateToUp -> navigateToUp()
            }
        }
    }

    TutorialScreen(
        bottomPadding = bottomPadding,
        onCancelClick = viewModel::onCancelClicked,
        modifier = modifier
    )
}

@Composable
private fun TutorialScreen(
    bottomPadding: Dp,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = ByeBooTheme.colors.black)
            .padding(horizontal = 24.dp)
            .padding(top = 67.dp, bottom = bottomPadding)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_cancel),
            contentDescription = "",
            tint = ByeBooTheme.colors.white,
            modifier = Modifier
                .align(Alignment.End)
                .clickable(onClick = onCancelClick)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = (14.5).dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            for (contents in TutorialContent.entries) {
                key(contents) {
                    Image(
                        painter = painterResource(id = contents.image),
                        contentDescription = "",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = contents.content,
                        style = ByeBooTheme.typography.body3,
                        color = ByeBooTheme.colors.tutorialPurple,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
