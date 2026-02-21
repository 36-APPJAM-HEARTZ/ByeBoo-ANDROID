package com.byeboo.app.presentation.splash.termsofservice

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.component.backhandler.ByeBooBackHandler
import com.byeboo.app.core.designsystem.component.button.ByeBooActivationButton
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.openUrl
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.splash.termsofservice.component.TermsAllButton
import com.byeboo.app.presentation.splash.termsofservice.component.TermsCheckButton

@Composable
fun TermsOfServiceRoute(
    navigateToUserInfo: () -> Unit,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: TermsOfServiceViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ByeBooBackHandler()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is TermsOfServiceSideEffect.OpenUrl -> openUrl(context = context, effect.url)
                is TermsOfServiceSideEffect.NavigateToUserInfo -> navigateToUserInfo()
            }
        }
    }

    TermsOfServiceScreen(
        uiState = uiState,
        paddingValues = paddingValues,
        onTermsAllClicked = viewModel::onAllTermsClick,
        onCheckClick = { term -> viewModel.onTermsClick(term) },
        onTermsLinkClick = { url -> viewModel.onTermsLinkClicked(url) },
        onNextButton = viewModel::onCompleteButtonClick,
        modifier = modifier,
    )
}

@Composable
private fun TermsOfServiceScreen(
    uiState: TermsOfServiceUiState,
    paddingValues: PaddingValues,
    onTermsAllClicked: () -> Unit,
    onCheckClick: (TermType) -> Unit,
    onTermsLinkClick: (String?) -> Unit,
    onNextButton: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxSize(),
    ) {
        Image(
            painter = painterResource(R.drawable.img_bg_userinfo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        Column(
            modifier =
                Modifier
                    .padding(horizontal = screenWidthDp(24.dp))
                    .padding(
                        top = paddingValues.calculateTopPadding() + screenHeightDp(67.dp),
                        bottom = paddingValues.calculateBottomPadding(),
                    ).fillMaxSize(),
        ) {
            TermsHeader()

            TermsAllButton(
                onTermsAllClick = onTermsAllClicked,
                isChecked = uiState.isAllChecked,
            )

            Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

            Column(
                modifier = Modifier.padding(horizontal = screenWidthDp(15.dp)),
            ) {
                TermType.entries.forEach { term ->
                    TermsCheckButton(
                        title = term.content,
                        hasMoreText = term.hasMoreText,
                        isSelected = uiState.isChecked(term),
                        onCheckClick = { onCheckClick(term) },
                        onLinkClick = { onTermsLinkClick(term.link) },
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            ByeBooActivationButton(
                buttonDisableColor = ByeBooTheme.colors.blackAlpha50,
                buttonText = "다음으로",
                buttonDisableTextColor = ByeBooTheme.colors.gray400,
                isEnabled = uiState.isAllChecked,
                onClick = onNextButton,
            )

            Spacer(modifier = Modifier.height(screenHeightDp(10.dp)))
        }
    }
}

@Composable
private fun TermsHeader() {
    Column(
        modifier = Modifier.padding(vertical = screenHeightDp(20.dp)),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "필수 약관에 동의해 주세요",
            style = ByeBooTheme.typography.head1,
            color = ByeBooTheme.colors.gray50,
        )

        Spacer(modifier = Modifier.height(screenHeightDp(8.dp)))

        Text(
            text = "Bye Boo 이용을 위해 필요해요",
            style = ByeBooTheme.typography.body6,
            color = ByeBooTheme.colors.gray400,
        )
    }
}
