package com.byeboo.app.presentation.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.event.LocalSnackBarTrigger
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.openUrl
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.mypage.component.MyPageModal
import com.byeboo.app.presentation.mypage.component.MyPageNotification

@Composable
fun MyPageRoute(
    navigateToEditProfile: () -> Unit,
    navigateToOffboardingCompletedJourney: () -> Unit,
    navigateToTutorial: () -> Unit,
    navigateToSplash: () -> Unit,
    bottomPadding: Dp,
    modifier: Modifier = Modifier,
    viewModel: MyPageViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val showSnackBar = LocalSnackBarTrigger.current

    if (uiState.showLogoutModal) {
        MyPageModal(
            onDismissRequest = { viewModel.onDismissModal(ModalType.LOGOUT) },
            myPageModalMainText = "로그아웃하시겠어요?",
            onCancelClick = { viewModel.onDismissModal(ModalType.LOGOUT) },
            onConfirmClick = viewModel::confirmLogout,
            onConfirmText = "로그아웃",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = screenWidthDp(48.dp)),
            dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
        )
    }

    if (uiState.showDeleteAccountModal) {
        MyPageModal(
            onDismissRequest = { viewModel.onDismissModal(ModalType.DELETE_ACCOUNT) },
            myPageModalMainText = "정말 탈퇴하시겠어요?",
            onCancelClick = { viewModel.onDismissModal(ModalType.DELETE_ACCOUNT) },
            onConfirmClick = viewModel::confirmWithdraw,
            onConfirmText = "탈퇴하기",
            myPageModalSubText = "탈퇴 시 모든 데이터가 삭제됩니다.",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = screenWidthDp(48.dp)),
            dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
        )
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is MyPageSideEffect.OpenUrl -> openUrl(context = context, effect.url)
                is MyPageSideEffect.NavigateToEditProfile -> navigateToEditProfile()
                is MyPageSideEffect.NavigateToOffboardingCompletedJourney -> navigateToOffboardingCompletedJourney()
                is MyPageSideEffect.NavigateToTutorial -> navigateToTutorial()
                is MyPageSideEffect.NavigateToSplash -> navigateToSplash()
                is MyPageSideEffect.ShowSnackBar -> showSnackBar(effect.message)
            }
        }
    }

    MyPageScreen(
        uiState = uiState,
        bottomPadding = bottomPadding,
        onNicknameChangeClick = viewModel::onNicknameChangeClicked,
        onCompletedJourneyClick = viewModel::onCompletedJourneyClicked,
        onGoToByeBooUniverseClick = viewModel::onGoToByeBooUniverseClicked,
        onAskingByeBooClick = viewModel::onAskingByeBooClicked,
        onServiceWithByeBooClick = viewModel::onServiceWithByeBooClicked,
        onPrivacyPolicyClick = viewModel::onPrivacyPolicyClicked,
        onTermsOfServiceClick = viewModel::onTermsOfServiceClicked,
        onLogoutClick = viewModel::onLogoutClicked,
        onDeleteAccountClick = viewModel::onDeleteAccountClicked,
        modifier = modifier
    )
}

@Composable
private fun MyPageScreen(
    uiState: MyPageState,
    bottomPadding: Dp,
    onNicknameChangeClick: () -> Unit,
    onCompletedJourneyClick: () -> Unit,
    onGoToByeBooUniverseClick: () -> Unit,
    onAskingByeBooClick: () -> Unit,
    onServiceWithByeBooClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onTermsOfServiceClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isChecked by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ByeBooTheme.colors.black)
            .padding(horizontal = screenWidthDp(24.dp))
            .padding(top = 67.dp, bottom = bottomPadding)
    ) {
        Text(
            text = "내 정보",
            style = ByeBooTheme.typography.sub1,
            color = ByeBooTheme.colors.white,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(ByeBooTheme.colors.black)
                .padding(vertical = 16.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(color = ByeBooTheme.colors.whiteAlpha10)
                    .clickable(onClick = onNicknameChangeClick)
                    .padding(horizontal = screenWidthDp(24.dp), vertical = 18.5.dp)
            ) {
                Text(
                    text = uiState.nickname,
                    style = ByeBooTheme.typography.body3,
                    color = ByeBooTheme.colors.gray300
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_right),
                    contentDescription = "",
                    modifier = Modifier.size(20.dp),
                    tint = ByeBooTheme.colors.gray50
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                thickness = 1.dp,
                color = ByeBooTheme.colors.whiteAlpha10
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_tip_write),
                    contentDescription = "",
                    tint = Color.Unspecified
                )

                Spacer(modifier = Modifier.width(screenWidthDp(8.dp)))

                Text(
                    text = "나의 기록",
                    style = ByeBooTheme.typography.body1,
                    color = ByeBooTheme.colors.gray300
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(color = ByeBooTheme.colors.whiteAlpha10)
                    .border(
                        width = 1.dp,
                        color = ByeBooTheme.colors.primary300,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable(onClick = onCompletedJourneyClick)
                    .padding(horizontal = screenWidthDp(24.dp), vertical = 20.dp)
            ) {
                Text(
                    text = "완료한 여정 돌아보기",
                    style = ByeBooTheme.typography.body2,
                    color = ByeBooTheme.colors.gray50
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_change),
                    contentDescription = "",
                    tint = Color.Unspecified
                )

                Spacer(modifier = Modifier.width(screenWidthDp(8.dp)))

                Text(
                    text = "보리가 궁금하다면?",
                    style = ByeBooTheme.typography.body1,
                    color = ByeBooTheme.colors.gray300
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(color = ByeBooTheme.colors.whiteAlpha10)
                    .border(
                        width = 1.dp,
                        color = ByeBooTheme.colors.primary300,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable(onClick = onGoToByeBooUniverseClick)
                    .padding(horizontal = screenWidthDp(24.dp), vertical = 20.dp)
            ) {
                Text(
                    text = "Bye Boo 세계관 보러 가기",
                    style = ByeBooTheme.typography.body2,
                    color = ByeBooTheme.colors.gray50
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                thickness = 1.dp,
                color = ByeBooTheme.colors.whiteAlpha10
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "문의하기",
                style = ByeBooTheme.typography.body1,
                color = ByeBooTheme.colors.gray400
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "바이부에 문의하기",
                style = ByeBooTheme.typography.body3,
                color = ByeBooTheme.colors.gray50,
                modifier = Modifier.clickable(onClick = onAskingByeBooClick)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "바이부와 함께 서비스 만들기",
                style = ByeBooTheme.typography.body3,
                color = ByeBooTheme.colors.gray50,
                modifier = Modifier.clickable(onClick = onServiceWithByeBooClick)
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "알림",
                style = ByeBooTheme.typography.body1,
                color = ByeBooTheme.colors.gray400
            )

            Spacer(modifier = Modifier.height(16.dp))

            MyPageNotification(
                isEnabledAlarm = isChecked,
                onCheckedClick = {
                    isChecked = !isChecked
                }
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "약관 및 정책",
                style = ByeBooTheme.typography.body1,
                color = ByeBooTheme.colors.gray400
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "개인정보 처리 방침",
                style = ByeBooTheme.typography.body3,
                color = ByeBooTheme.colors.gray50,
                modifier = Modifier.clickable(onClick = onPrivacyPolicyClick)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "서비스 이용 약관",
                style = ByeBooTheme.typography.body3,
                color = ByeBooTheme.colors.gray50,
                modifier = Modifier.clickable(onClick = onTermsOfServiceClick)
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "계정",
                style = ByeBooTheme.typography.body1,
                color = ByeBooTheme.colors.gray400
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "로그아웃",
                style = ByeBooTheme.typography.body3,
                color = ByeBooTheme.colors.gray50,
                modifier = Modifier.clickable(onClick = onLogoutClick)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "탈퇴",
                style = ByeBooTheme.typography.body3,
                color = ByeBooTheme.colors.gray50,
                modifier = Modifier.clickable(onClick = onDeleteAccountClick)
            )

            Spacer(modifier = Modifier.height(38.dp))
        }
    }
}
