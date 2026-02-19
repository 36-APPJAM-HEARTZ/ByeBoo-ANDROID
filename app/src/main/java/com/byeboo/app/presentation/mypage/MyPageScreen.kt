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
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.remember
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
import com.byeboo.app.core.designsystem.event.LocalSnackBarTrigger
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
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

    val onAlarmToggleClicked =
        remember(context, viewModel, activity, permissionLauncher) {
            { _: Boolean ->
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
                        Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
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

    if (uiState.showPermissionModal) {
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

    if (uiState.showLogoutModal) {
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

    if (uiState.showDeleteAccountModal) {
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
        uiState = uiState,
        paddingValues = paddingValues,
        onNicknameChangeClick = viewModel::onNicknameChangeClicked,
        onCompletedJourneyClick = viewModel::onCompletedJourneyClicked,
        onGoToByeBooUniverseClick = viewModel::onGoToByeBooUniverseClicked,
        onAskingByeBooClick = viewModel::onAskingByeBooClicked,
        onServiceWithByeBooClick = viewModel::onServiceWithByeBooClicked,
        onAlarmToggleClick = onAlarmToggleClicked,
        onPrivacyPolicyClick = viewModel::onPrivacyPolicyClicked,
        onTermsOfServiceClick = viewModel::onTermsOfServiceClicked,
        onLogoutClick = viewModel::onLogoutClicked,
        onDeleteAccountClick = viewModel::onDeleteAccountClicked,
        modifier = modifier,
    )
}

@Composable
private fun MyPageScreen(
    uiState: MyPageState,
    paddingValues: PaddingValues,
    onNicknameChangeClick: () -> Unit,
    onCompletedJourneyClick: () -> Unit,
    onGoToByeBooUniverseClick: () -> Unit,
    onAskingByeBooClick: () -> Unit,
    onServiceWithByeBooClick: () -> Unit,
    onAlarmToggleClick: (Boolean) -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onTermsOfServiceClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(ByeBooTheme.colors.background)
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                ),
    ) {
        Text(
            text = "내 정보",
            color = ByeBooTheme.colors.white,
            style = ByeBooTheme.typography.sub1,
            textAlign = TextAlign.Center,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(ByeBooTheme.colors.background)
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
                    bottom = screenHeightDp(56.dp),
                ),
        ) {
            item {
                NicknameSection(
                    onNicknameChangeClick = onNicknameChangeClick,
                    nickname = uiState.nickname,
                )
            }

            item {
                HorizontalDivider(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = screenHeightDp(8.dp)),
                    thickness = 1.dp,
                    color = ByeBooTheme.colors.gray800,
                )
            }

            item {
                MyRecordingSection(onCompletedJourneyClick = onCompletedJourneyClick)
            }

            item {
                ByeBooUniverseSection(onGoToByeBooUniverseClick = onGoToByeBooUniverseClick)
            }

            item {
                HorizontalDivider(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = screenHeightDp(8.dp)),
                    thickness = 1.dp,
                    color = ByeBooTheme.colors.gray800,
                )
            }

            item {
                AskingSection(
                    onAskingByeBooClick = onAskingByeBooClick,
                    onServiceWithByeBooClick = onServiceWithByeBooClick,
                )
            }

            item {
                NotificationSection(
                    isAlarmEnabled = uiState.isAlarmEnabled,
                    onAlarmToggleClick = onAlarmToggleClick,
                )
            }

            item {
                TermsSection(
                    onPrivacyPolicyClick = onPrivacyPolicyClick,
                    onTermsOfServiceClick = onTermsOfServiceClick,
                )
            }

            item {
                AccountSection(
                    onLogoutClick = onLogoutClick,
                    onDeleteAccountClick = onDeleteAccountClick,
                )
            }
        }
    }
}

@Composable
private fun NicknameSection(
    onNicknameChangeClick: () -> Unit,
    nickname: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(color = ByeBooTheme.colors.whiteAlpha5)
                .clickable(onClick = onNicknameChangeClick)
                .padding(
                    horizontal = screenWidthDp(24.dp),
                    vertical = screenHeightDp(18.dp),
                ),
    ) {
        Text(
            text = nickname,
            color = ByeBooTheme.colors.gray100,
            style = ByeBooTheme.typography.body3,
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_right),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = ByeBooTheme.colors.gray50,
        )
    }

    Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))
}

@Composable
private fun MyRecordingSection(
    onCompletedJourneyClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))

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
            color = ByeBooTheme.colors.gray300,
            style = ByeBooTheme.typography.body1,
        )
    }

    Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(color = ByeBooTheme.colors.whiteAlpha5)
                .border(
                    width = 1.dp,
                    color = ByeBooTheme.colors.primary300,
                    shape = RoundedCornerShape(12.dp),
                ).clickable(onClick = onCompletedJourneyClick)
                .padding(
                    horizontal = screenWidthDp(24.dp),
                    vertical = screenHeightDp(20.dp),
                ),
    ) {
        Text(
            text = "완료한 여정 돌아보기",
            color = ByeBooTheme.colors.gray50,
            style = ByeBooTheme.typography.body2,
        )
    }
}

@Composable
private fun ByeBooUniverseSection(
    onGoToByeBooUniverseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))

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
            color = ByeBooTheme.colors.gray300,
            style = ByeBooTheme.typography.body1,
        )
    }

    Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(color = ByeBooTheme.colors.whiteAlpha5)
                .border(
                    width = 1.dp,
                    color = ByeBooTheme.colors.primary300,
                    shape = RoundedCornerShape(12.dp),
                ).clickable(onClick = onGoToByeBooUniverseClick)
                .padding(
                    horizontal = screenWidthDp(24.dp),
                    vertical = screenHeightDp(20.dp),
                ),
    ) {
        Text(
            text = "Bye Boo 세계관 보러 가기",
            color = ByeBooTheme.colors.gray50,
            style = ByeBooTheme.typography.body2,
        )
    }
    Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))
}

@Composable
private fun AskingSection(
    onAskingByeBooClick: () -> Unit,
    onServiceWithByeBooClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Spacer(modifier = Modifier.height(screenHeightDp(36.dp)))

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(screenHeightDp(16.dp))
    ) {
        Text(
            text = "문의하기",
            color = ByeBooTheme.colors.gray400,
            style = ByeBooTheme.typography.body1,
        )


        Text(
            text = "바이부에 문의하기",
            color = ByeBooTheme.colors.gray50,
            style = ByeBooTheme.typography.body3,
            modifier = Modifier.clickable(onClick = onAskingByeBooClick),
        )

        Text(
            text = "바이부와 함께 서비스 만들기",
            color = ByeBooTheme.colors.gray50,
            style = ByeBooTheme.typography.body3,
            modifier = Modifier.clickable(onClick = onServiceWithByeBooClick),
        )
    }
    Spacer(modifier = Modifier.height(screenHeightDp(24.dp)))
}

@Composable
private fun NotificationSection(
    isAlarmEnabled: Boolean?,
    onAlarmToggleClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Spacer(modifier = Modifier.height(screenHeightDp(24.dp)))

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(screenHeightDp(16.dp))
    ) {
        Text(
            text = "알림",
            color = ByeBooTheme.colors.gray400,
            style = ByeBooTheme.typography.body1,
        )


        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "퀘스트 오픈 알림",
                color = ByeBooTheme.colors.gray50,
                style = ByeBooTheme.typography.body3,
            )

            Spacer(modifier = Modifier.weight(1f))

            when (isAlarmEnabled) {
                null -> {
                    Box(
                        modifier =
                            Modifier
                                .size(width = 48.dp, height = 28.dp)
                                .background(Color.Transparent),
                    )
                }

                else -> {
                    NotificationToggle(
                        isToggleOn = isAlarmEnabled,
                        onToggleClicked = onAlarmToggleClick,
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(screenHeightDp(24.dp)))
}

@Composable
private fun TermsSection(
    onPrivacyPolicyClick: () -> Unit,
    onTermsOfServiceClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Spacer(modifier = Modifier.height(screenHeightDp(24.dp)))

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(screenHeightDp(16.dp))

    ) {
        Text(
            text = "약관 및 정책",
            color = ByeBooTheme.colors.gray400,
            style = ByeBooTheme.typography.body1,
        )

        Text(
            text = "개인정보 처리 방침",
            color = ByeBooTheme.colors.gray50,
            style = ByeBooTheme.typography.body3,
            modifier = Modifier.clickable(onClick = onPrivacyPolicyClick),
        )

        Text(
            text = "서비스 이용 약관",
            color = ByeBooTheme.colors.gray50,
            style = ByeBooTheme.typography.body3,
            modifier = Modifier.clickable(onClick = onTermsOfServiceClick),
        )
    }
    Spacer(modifier = Modifier.height(screenHeightDp(24.dp)))
}

@Composable
private fun AccountSection(
    onLogoutClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Spacer(modifier = Modifier.height(screenHeightDp(24.dp)))

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(screenHeightDp(16.dp))
    ) {
        Text(
            text = "계정",
            color = ByeBooTheme.colors.gray400,
            style = ByeBooTheme.typography.body1,
        )

        Text(
            text = "로그아웃",
            color = ByeBooTheme.colors.gray50,
            style = ByeBooTheme.typography.body3,
            modifier = Modifier.clickable(onClick = onLogoutClick),
        )

        Text(
            text = "탈퇴",
            color = ByeBooTheme.colors.gray50,
            style = ByeBooTheme.typography.body3,
            modifier = Modifier.clickable(onClick = onDeleteAccountClick),
        )
    }
}
