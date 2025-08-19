package com.byeboo.app.presentation.splash.termsofservice

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.component.button.ByeBooActivationButton
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.openUrl
import com.byeboo.app.presentation.splash.termsofservice.component.TermsAllButton
import com.byeboo.app.presentation.splash.termsofservice.component.TermsCheckButton

@Composable
fun TermsOfServiceRoute(
    padding: Dp,
    modifier: Modifier = Modifier,
    viewModel: TermsOfServiceViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    TermsOfServiceScreen(
        uiState = uiState,
        padding = padding,
        onAllTermsClick = viewModel::onAllTermsClick,
        onCheckClick = { term -> viewModel.onTermsClick(term) },
        onLinkClick = { url -> openUrl(context, url) },
        modifier = modifier
    )

}

@Composable
private fun TermsOfServiceScreen(
    uiState: TermsOfServiceUiState,
    padding: Dp,
    onAllTermsClick: () -> Unit,
    onCheckClick : (TermType) -> Unit,
    onLinkClick: (String) -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.bg_userinfo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.padding(horizontal = 24.dp)
                .padding(top = 67.dp, bottom = padding)
                .fillMaxSize()
        ) {
            TermsHeader()

            TermsAllButton(
                isChecked = uiState.isAllChecked,
                onClick = { onAllTermsClick() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.padding(horizontal = 15.dp)
            ) {
                TermType.entries.forEach { term ->
                    TermsCheckButton(
                        title = term.content,
                        hasMoreText = term.hasMoreText,
                        isSelected = uiState.isChecked(term),
                        onCheckClick = {onCheckClick(term)},
                        onLinkClick = { term.link?.let { onLinkClick(it) } }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            ByeBooActivationButton(
                buttonDisableColor = ByeBooTheme.colors.blackAlpha50,
                buttonText = "다음으로",
                buttonDisableTextColor = ByeBooTheme.colors.gray400,
                isEnabled = uiState.isAllChecked,
                onClick = {}
            )
        }
    }
}

@Composable
private fun TermsHeader() {
    Column(
        modifier = Modifier.padding(vertical = 20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "필수 약관에 동의해 주세요",
            style = ByeBooTheme.typography.head1,
            color = ByeBooTheme.colors.gray50
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Bye Boo 이용을 위해 필요해요",
            style = ByeBooTheme.typography.body6,
            color = ByeBooTheme.colors.gray400
        )
    }
}

