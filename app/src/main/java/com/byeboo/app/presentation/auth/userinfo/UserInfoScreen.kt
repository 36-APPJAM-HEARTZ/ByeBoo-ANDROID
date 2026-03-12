package com.byeboo.app.presentation.auth.userinfo

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.component.backhandler.ByeBooBackHandler
import com.byeboo.app.core.designsystem.component.button.ByeBooActivationButton
import com.byeboo.app.core.designsystem.event.LocalSnackBarTrigger
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.model.quest.JourneyType
import com.byeboo.app.core.util.addFocusCleaner
import com.byeboo.app.core.util.noRippleClickable
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.domain.model.auth.NicknameValidationResult
import com.byeboo.app.presentation.auth.userinfo.component.StepProgressBar
import com.byeboo.app.presentation.auth.userinfo.model.toValidationState
import com.byeboo.app.presentation.auth.userinfo.screen.UserInfoNicknameScreen
import com.byeboo.app.presentation.auth.userinfo.screen.UserInfoQuestScreen
import kotlinx.coroutines.launch

@Composable
fun UserInfoRoute(
    navigateToLoading: () -> Unit,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    viewModel: UserInfoViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val showSnackBar = LocalSnackBarTrigger.current
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is UserInfoSideEffect.NavigateToLoading -> navigateToLoading()
                is UserInfoSideEffect.NavigateToNextPage -> {
                    pagerState.scrollToPage(1)
                }
                is UserInfoSideEffect.ShowSnackBar -> {
                    showSnackBar(effect.snackBarType)
                }
            }
        }
    }

    UserInfoScreen(
        uiState = uiState,
        pagerState = pagerState,
        onNicknameChange = viewModel::updateNickname,
        onNicknameComplete = viewModel::onNicknameComplete,
        onQuestSelect = viewModel::updateQuest,
        onSubmit = viewModel::finishUserInfo,
        modifier = modifier,
        paddingValues = paddingValues,
    )
}

@Composable
private fun UserInfoScreen(
    uiState: UserInfoState,
    pagerState: PagerState,
    onNicknameChange: (String) -> Unit,
    onNicknameComplete: () -> Unit,
    onQuestSelect: (JourneyType) -> Unit,
    onSubmit: () -> Unit,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    val isStepValid by remember(
        pagerState.currentPage,
        uiState.nicknameValidation,
        uiState.selectedQuest,
    ) {
        derivedStateOf {
            when (pagerState.currentPage) {
                0 -> uiState.nicknameValidation == NicknameValidationResult.Valid
                1 -> uiState.selectedQuest != null
                else -> false
            }
        }
    }

    if (pagerState.currentPage != 0) {
        BackHandler {
            coroutineScope.launch {
                pagerState.scrollToPage(pagerState.currentPage - 1)
            }
        }
    } else {
        ByeBooBackHandler()
    }

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .addFocusCleaner(focusManager),
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
                    .fillMaxSize()
                    .padding(horizontal = screenWidthDp(24.dp))
                    .padding(top = paddingValues.calculateTopPadding() + screenHeightDp(43.dp)),
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(screenHeightDp(24.dp)),
            ) {
                if (pagerState.currentPage != 0) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_left),
                        contentDescription = "뒤로가기",
                        tint = ByeBooTheme.colors.white,
                        modifier =
                            Modifier
                                .size(24.dp)
                                .noRippleClickable {
                                    coroutineScope.launch {
                                        pagerState.scrollToPage(pagerState.currentPage - 1)
                                    }
                                },
                    )
                }
            }

            Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

            StepProgressBar(currentStep = pagerState.currentPage + 1)

            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
            ) { page ->
                when (page) {
                    0 ->
                        UserInfoNicknameScreen(
                            nickname = uiState.nickname,
                            validationState = uiState.nicknameValidation.toValidationState(),
                            onTextChange = onNicknameChange,
                        )

                    1 ->
                        UserInfoQuestScreen(
                            selectedQuest = uiState.selectedQuest,
                            onQuestSelect = onQuestSelect,
                        )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            ByeBooActivationButton(
                modifier =
                    Modifier.padding(
                        bottom = paddingValues.calculateBottomPadding() + screenHeightDp(10.dp),
                    ),
                buttonDisableColor = ByeBooTheme.colors.blackAlpha50,
                buttonDisableTextColor = ByeBooTheme.colors.gray400,
                isEnabled = isStepValid,
                buttonText = if (pagerState.currentPage == 0) "다음으로" else "완료하기",
                onClick = {
                    if (pagerState.currentPage == 0) {
                        onNicknameComplete()
                    } else {
                        onSubmit()
                    }
                },
            )
        }
    }
}
