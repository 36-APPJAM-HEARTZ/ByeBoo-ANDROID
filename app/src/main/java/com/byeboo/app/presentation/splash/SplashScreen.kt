package com.byeboo.app.presentation.splash

import android.Manifest.permission.POST_NOTIFICATIONS
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.event.LocalSnackBarTrigger
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.noRippleClickable
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.kakao.sdk.user.UserApiClient

@Composable
fun SplashRoute(
    navigateToHome: () -> Unit,
    navigateToUserInfo: () -> Unit,
    navigateToTermsOfService: () -> Unit,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val showSnackBar = LocalSnackBarTrigger.current
    var showLoginButton by remember { mutableStateOf(false) }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                viewModel.onPermissionResult(isGranted)
            },
        )

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is SplashStateSideEffect.ShowLoginButton -> {
                    showLoginButton = true
                }

                is SplashStateSideEffect.NavigateToHome -> navigateToHome()
                is SplashStateSideEffect.NavigateToUserInfo -> navigateToUserInfo()
                is SplashStateSideEffect.NavigateToTermsOfService -> navigateToTermsOfService()
                is SplashStateSideEffect.StartKakaoTalkLogin -> {
                    UserApiClient.instance.loginWithKakaoTalk(
                        context = context,
                        callback = viewModel::updateLoginResult,
                    )
                }

                is SplashStateSideEffect.StartKakaoWebLogin -> {
                    UserApiClient.instance.loginWithKakaoAccount(
                        context = context,
                        callback = viewModel::updateLoginResult,
                    )
                }

                is SplashStateSideEffect.RequestNotificationPermission -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionLauncher.launch(POST_NOTIFICATIONS)
                    } else {
                        viewModel.onPermissionResult(true)
                    }
                }

                is SplashStateSideEffect.ShowSnackBar -> showSnackBar(effect.snackBarType)
            }
        }
    }

    SplashScreen(
        paddingValues = paddingValues,
        showLoginButton = showLoginButton,
        onClick = {
            val availableButton = UserApiClient.instance.isKakaoTalkLoginAvailable(context)
            viewModel.startKakaoLogin(availableButton)
        },
        modifier = modifier,
    )
}

@Composable
private fun SplashScreen(
    paddingValues: PaddingValues,
    showLoginButton: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val upAnimation by animateDpAsState(
        targetValue = if (showLoginButton) (-24).dp else 0.dp,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "upShift",
    )

    val buttonAlpha by animateDpAsState(
        targetValue = if (showLoginButton) 1.dp else 0.dp,
        animationSpec =
            tween(
                durationMillis = 450,
                delayMillis = 120,
                easing = LinearOutSlowInEasing,
            ),
        label = "buttonAlpha",
    )

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Image(
            painter = painterResource(R.drawable.img_bg_userinfo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.img_splash_logo),
            contentDescription = null,
            modifier =
                Modifier
                    .padding(horizontal = screenWidthDp(76.dp))
                    .padding(top = screenHeightDp(333.dp))
                    .offset(y = upAnimation),
        )

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = screenWidthDp(24.dp))
                    .padding(
                        bottom = screenHeightDp(10.dp) + paddingValues.calculateBottomPadding(),
                    ).offset(y = upAnimation),
        ) {
            Spacer(modifier = Modifier.weight(1f))

            if (showLoginButton) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(color = ByeBooTheme.colors.kakaoYellow)
                            .graphicsLayer { alpha = buttonAlpha.toPx() }
                            .noRippleClickable(onClick = onClick)
                            .padding(vertical = screenHeightDp(16.dp)),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_kakao_logo),
                        contentDescription = "kakao logo",
                    )

                    Spacer(modifier = Modifier.width(screenWidthDp(16.dp)))

                    Text(
                        text = "Kakao로 시작하기",
                        style = ByeBooTheme.typography.body2,
                        color = ByeBooTheme.colors.black,
                    )
                }
            }
        }
    }
}
