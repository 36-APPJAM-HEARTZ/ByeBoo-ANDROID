package com.byeboo.app.presentation.mypage

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.component.LoadingScreen
import com.byeboo.app.core.designsystem.event.LocalSnackBarTrigger
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.state.UiState
import com.byeboo.app.core.util.hasNotificationPermission
import com.byeboo.app.core.util.openUrl
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.mypage.component.BasicNotificationModal
import com.byeboo.app.presentation.mypage.component.MyPageModal
import com.byeboo.app.presentation.mypage.component.NotificationToggle

@Composable
fun MyPageRoute(
    navigateToEditProfile: () -> Unit,
    navigateToOffboardingCompletedJourney: () -> Unit,
    navigateToTutorial: () -> Unit,
    navigateToSplash: () -> Unit,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: MyPageViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val showSnackBar = LocalSnackBarTrigger.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val activity = context as? Activity

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                if (isGranted) {
                    viewModel.onPermissionResult(true)
                } else {
                    val showRationale =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            activity?.shouldShowRequestPermissionRationale(POST_NOTIFICATIONS) == true
                        } else {
                            false
                        }

                    if (!showRationale) {
                        viewModel.onAlarmToggledClicked(hasSystemPermission = false)
                    } else {
                        viewModel.onPermissionResult(false)
                    }
                }
            },
        )

    val onAlarmToggleClicked = {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (context.hasNotificationPermission()) {
                viewModel.onAlarmToggledClicked(true)
            } else {
                val shouldShowRationale =
                    activity?.shouldShowRequestPermissionRationale(POST_NOTIFICATIONS) == true

                if (shouldShowRationale) {
                    viewModel.onAlarmToggledClicked(false)
                } else {
                    permissionLauncher.launch(POST_NOTIFICATIONS)
                }
            }
        } else {
            viewModel.onAlarmToggledClicked(true)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is MyPageSideEffect.RequestNotificationPermission -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionLauncher.launch(POST_NOTIFICATIONS)
                    }
                }

                is MyPageSideEffect.NavigateToSetting -> {
                    val intent =
                        Intent(Settings.ACTION_ALL_APPS_NOTIFICATION_SETTINGS).apply {
                            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                        }
                    context.startActivity(intent)
                }

                is MyPageSideEffect.OpenUrl -> openUrl(context = context, effect.url)
                is MyPageSideEffect.NavigateToEditProfile -> navigateToEditProfile()
                is MyPageSideEffect.NavigateToOffboardingCompletedJourney -> navigateToOffboardingCompletedJourney()
                is MyPageSideEffect.NavigateToTutorial -> navigateToTutorial()
                is MyPageSideEffect.NavigateToSplash -> navigateToSplash()
                is MyPageSideEffect.ShowSnackBar -> showSnackBar(effect.message)
            }
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer =
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    viewModel.syncAlarmState(context.hasNotificationPermission())
                }
            }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    when (val state = uiState) {
        is UiState.Loading -> {
            LoadingScreen()
        }

        is UiState.Failure -> Unit

        is UiState.Success -> {
            val myPageState = state.data

            if (myPageState.showPermissionModal) {
                BasicNotificationModal(
                    onDismissRequest = { viewModel.onDismissModal(ModalType.PERMISSION) },
                    onConfirmClick = {
                        viewModel.onDismissModal(ModalType.PERMISSION)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            val shouldShowRationale =
                                activity?.shouldShowRequestPermissionRationale(POST_NOTIFICATIONS) == true

                            if (shouldShowRationale) {
                                permissionLauncher.launch(POST_NOTIFICATIONS)
                            } else {
                                viewModel.onGoToSettingClicked()
                            }
                        }
                    },
                )
            }

            if (myPageState.showLogoutModal) {
                MyPageModal(
                    onDismissRequest = { viewModel.onDismissModal(ModalType.LOGOUT) },
                    myPageModalMainText = "로그아웃하시겠어요?",
                    onCancelClick = { viewModel.onDismissModal(ModalType.LOGOUT) },
                    onConfirmClick = viewModel::confirmLogout,
                    onConfirmText = "로그아웃",
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = screenWidthDp(48.dp)),
                    dialogProperties = DialogProperties(usePlatformDefaultWidth = false),
                )
            }

            if (myPageState.showDeleteAccountModal) {
                MyPageModal(
                    onDismissRequest = { viewModel.onDismissModal(ModalType.DELETE_ACCOUNT) },
                    myPageModalMainText = "정말 탈퇴하시겠어요?",
                    onCancelClick = { viewModel.onDismissModal(ModalType.DELETE_ACCOUNT) },
                    onConfirmClick = viewModel::confirmWithdraw,
                    onConfirmText = "탈퇴하기",
                    myPageModalSubText = "탈퇴 시 모든 데이터가 삭제됩니다.",
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = screenWidthDp(48.dp)),
                    dialogProperties = DialogProperties(usePlatformDefaultWidth = false),
                )
            }

            MyPageScreen(
                uiState = myPageState,
                paddingValues = paddingValues,
                onNicknameChangeClicked = viewModel::onNicknameChangeClicked,
                onCompletedJourneyClicked = viewModel::onCompletedJourneyClicked,
                onGoToByeBooUniverseClicked = viewModel::onGoToByeBooUniverseClicked,
                onAskingByeBooClicked = viewModel::onAskingByeBooClicked,
                onServiceWithByeBooClicked = viewModel::onServiceWithByeBooClicked,
                onAlarmToggleClicked = onAlarmToggleClicked,
                onPrivacyPolicyClicked = viewModel::onPrivacyPolicyClicked,
                onTermsOfServiceClicked = viewModel::onTermsOfServiceClicked,
                onLogoutClicked = viewModel::onLogoutClicked,
                onDeleteAccountClicked = viewModel::onDeleteAccountClicked,
                modifier = modifier,
            )
        }
        else -> Unit

    }
}

@Composable
private fun MyPageScreen(
    uiState: MyPageState,
    paddingValues: PaddingValues,
    onNicknameChangeClicked: () -> Unit,
    onCompletedJourneyClicked: () -> Unit,
    onGoToByeBooUniverseClicked: () -> Unit,
    onAskingByeBooClicked: () -> Unit,
    onServiceWithByeBooClicked: () -> Unit,
    onAlarmToggleClicked: () -> Unit,
    onPrivacyPolicyClicked: () -> Unit,
    onTermsOfServiceClicked: () -> Unit,
    onLogoutClicked: () -> Unit,
    onDeleteAccountClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(ByeBooTheme.colors.black)
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                ),
    ) {
        Text(
            text = "내 정보",
            style = ByeBooTheme.typography.sub1,
            color = ByeBooTheme.colors.white,
            textAlign = TextAlign.Center,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(ByeBooTheme.colors.black)
                    .padding(horizontal = screenWidthDp(24.dp))
                    .padding(top = screenHeightDp(43.dp), bottom = screenHeightDp(16.dp)),
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding =
                PaddingValues(
                    start = screenWidthDp(24.dp),
                    top = screenHeightDp(8.dp),
                    end = screenWidthDp(24.dp),
                    bottom = screenHeightDp(34.dp),
                ),
        ) {
            item {
                NicknameSection(
                    onNicknameChangeClicked = onNicknameChangeClicked,
                    nickname = uiState.nickname
                )

                Spacer(modifier = Modifier.height(screenHeightDp(8.dp)))
            }

            item {
                HorizontalDivider(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = screenHeightDp(8.dp)),
                    thickness = 1.dp,
                    color = ByeBooTheme.colors.whiteAlpha10,
                )

                Spacer(modifier = Modifier.height(screenHeightDp(24.dp)))

            }

            item {
                MyRecordingSection(onCompletedJourneyClicked = onCompletedJourneyClicked)

                Spacer(modifier = Modifier.height(screenHeightDp(24.dp)))

            }

            item {
                ByeBooUniverseSection(onGoToByeBooUniverseClicked = onGoToByeBooUniverseClicked)

                Spacer(modifier = Modifier.height(screenHeightDp(24.dp)))

            }

            item {
                HorizontalDivider(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = screenHeightDp(8.dp)),
                    thickness = 1.dp,
                    color = ByeBooTheme.colors.whiteAlpha10,
                )

                Spacer(modifier = Modifier.height(screenHeightDp(24.dp)))
            }

            item {
                AskingSection(
                    onAskingByeBooClicked = onAskingByeBooClicked,
                    onServiceWithByeBooClicked = onServiceWithByeBooClicked
                )

                Spacer(modifier = Modifier.height(screenHeightDp(48.dp)))
            }

            item {
                NotificationSection(
                    isAlarmEnabled = uiState.isAlarmEnabled,
                    onAlarmToggleClicked = onAlarmToggleClicked
                )

                Spacer(modifier = Modifier.height(screenHeightDp(48.dp)))
            }

            item {
                TermsSection(
                    onPrivacyPolicyClicked = onPrivacyPolicyClicked,
                    onTermsOfServiceClicked = onTermsOfServiceClicked
                )

                Spacer(modifier = Modifier.height(screenHeightDp(48.dp)))
            }

            item {
                AccountSection(
                    onLogoutClicked = onLogoutClicked,
                    onDeleteAccountClicked = onDeleteAccountClicked
                )
            }
        }
    }
}

@Composable
private fun NicknameSection(
    onNicknameChangeClicked: () -> Unit,
    nickname: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(color = ByeBooTheme.colors.whiteAlpha10)
                .clickable(onClick = onNicknameChangeClicked)
                .padding(
                    horizontal = screenWidthDp(24.dp),
                    vertical = screenHeightDp(18.5.dp),
                ),
    ) {
        Text(
            text = nickname,
            style = ByeBooTheme.typography.body3,
            color = ByeBooTheme.colors.gray300,
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_right),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = ByeBooTheme.colors.gray50,
        )
    }
}

@Composable
private fun MyRecordingSection(
    onCompletedJourneyClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_tip_write),
            contentDescription = null,
            tint = Color.Unspecified,
        )

        Spacer(modifier = Modifier.width(screenWidthDp(8.dp)))

        Text(
            text = "나의 기록",
            style = ByeBooTheme.typography.body1,
            color = ByeBooTheme.colors.gray300,
        )
    }

    Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(color = ByeBooTheme.colors.whiteAlpha10)
                .border(
                    width = 1.dp,
                    color = ByeBooTheme.colors.primary300,
                    shape = RoundedCornerShape(12.dp),
                ).clickable(onClick = onCompletedJourneyClicked)
                .padding(
                    horizontal = screenWidthDp(24.dp),
                    vertical = screenHeightDp(20.dp),
                ),
    ) {
        Text(
            text = "완료한 여정 돌아보기",
            style = ByeBooTheme.typography.body2,
            color = ByeBooTheme.colors.gray50,
        )
    }
}

@Composable
private fun ByeBooUniverseSection(
    onGoToByeBooUniverseClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_change),
            contentDescription = null,
            tint = Color.Unspecified,
        )

        Spacer(modifier = Modifier.width(screenWidthDp(8.dp)))

        Text(
            text = "보리가 궁금하다면?",
            style = ByeBooTheme.typography.body1,
            color = ByeBooTheme.colors.gray300,
        )
    }

    Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(color = ByeBooTheme.colors.whiteAlpha10)
                .border(
                    width = 1.dp,
                    color = ByeBooTheme.colors.primary300,
                    shape = RoundedCornerShape(12.dp),
                ).clickable(onClick = onGoToByeBooUniverseClicked)
                .padding(
                    horizontal = screenWidthDp(24.dp),
                    vertical = screenHeightDp(20.dp),
                ),
    ) {
        Text(
            text = "Bye Boo 세계관 보러 가기",
            style = ByeBooTheme.typography.body2,
            color = ByeBooTheme.colors.gray50,
        )
    }

}

@Composable
private fun AskingSection(
    onAskingByeBooClicked: () -> Unit,
    onServiceWithByeBooClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "문의하기",
            style = ByeBooTheme.typography.body1,
            color = ByeBooTheme.colors.gray400,
        )

        Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

        Text(
            text = "바이부에 문의하기",
            style = ByeBooTheme.typography.body3,
            color = ByeBooTheme.colors.gray50,
            modifier = Modifier.clickable(onClick = onAskingByeBooClicked),
        )

        Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

        Text(
            text = "바이부와 함께 서비스 만들기",
            style = ByeBooTheme.typography.body3,
            color = ByeBooTheme.colors.gray50,
            modifier = Modifier.clickable(onClick = onServiceWithByeBooClicked),
        )
    }
}

@Composable
private fun NotificationSection(
    isAlarmEnabled: Boolean,
    onAlarmToggleClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
        ) {
        Text(
            text = "알림",
            style = ByeBooTheme.typography.body1,
            color = ByeBooTheme.colors.gray400,
        )

        Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,

            ) {
            Text(
                text = "퀘스트 오픈 알림",
                style = ByeBooTheme.typography.body3,
                color = ByeBooTheme.colors.gray50
            )

            Spacer(modifier = Modifier.weight(1f))

            NotificationToggle(
                isToggleOn = isAlarmEnabled,
                onToggleClicked = { onAlarmToggleClicked() }
            )
        }
    }
}

@Composable
private fun TermsSection(
    onPrivacyPolicyClicked: () -> Unit,
    onTermsOfServiceClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "약관 및 정책",
            style = ByeBooTheme.typography.body1,
            color = ByeBooTheme.colors.gray400,
        )

        Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

        Text(
            text = "개인정보 처리 방침",
            style = ByeBooTheme.typography.body3,
            color = ByeBooTheme.colors.gray50,
            modifier = Modifier.clickable(onClick = onPrivacyPolicyClicked),
        )

        Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

        Text(
            text = "서비스 이용 약관",
            style = ByeBooTheme.typography.body3,
            color = ByeBooTheme.colors.gray50,
            modifier = Modifier.clickable(onClick = onTermsOfServiceClicked),
        )
    }
}

@Composable
private fun AccountSection(
    onLogoutClicked: () -> Unit,
    onDeleteAccountClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "계정",
            style = ByeBooTheme.typography.body1,
            color = ByeBooTheme.colors.gray400,
        )

        Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

        Text(
            text = "로그아웃",
            style = ByeBooTheme.typography.body3,
            color = ByeBooTheme.colors.gray50,
            modifier = Modifier.clickable(onClick = onLogoutClicked),
        )

        Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

        Text(
            text = "탈퇴",
            style = ByeBooTheme.typography.body3,
            color = ByeBooTheme.colors.gray50,
            modifier = Modifier.clickable(onClick = onDeleteAccountClicked),
        )
    }
}
