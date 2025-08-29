package com.byeboo.app.presentation.splash

import android.app.ProgressDialog.show
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
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
    padding: Dp,
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val showSnackBar = LocalSnackBarTrigger.current

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is SplashStateSideEffect.NavigateToHome -> navigateToHome()
                is SplashStateSideEffect.NavigateToUserInfo -> navigateToUserInfo()
                is SplashStateSideEffect.NavigateToTermsOfService -> navigateToTermsOfService()
                is SplashStateSideEffect.StartKakaoTalkLogin ->  {
                    UserApiClient.instance.loginWithKakaoTalk(
                        context = context,
                        callback = viewModel::updateLoginResult
                    )
                }
                is SplashStateSideEffect.StartKakaoWebLogin ->  {
                    UserApiClient.instance.loginWithKakaoAccount(
                        context = context,
                        callback = viewModel::updateLoginResult
                    )
                }
                is SplashStateSideEffect.ShowSnackBar -> showSnackBar(sideEffect.message)
            }
        }
    }

    SplashScreen(
        padding = padding,
        onClick = {
            viewModel.startKakaoLogin(UserApiClient.instance.isKakaoTalkLoginAvailable(context))
        },
        modifier = modifier,
    )
}

@Composable
private fun SplashScreen(
    padding: Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.bg_userinfo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Image(
            painter = painterResource(R.drawable.img_splash_logo),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = screenWidthDp(76.dp))
                .padding(top = screenHeightDp(padding + 250.dp))
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(bottom = padding),
        ) {

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(color = ByeBooTheme.colors.kakaoYellow)
                    .noRippleClickable(onClick = onClick)
                    .padding(vertical = 16.dp)
                ,
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_kakao_logo),
                    contentDescription = "kakao logo"
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Kakao로 시작하기",
                    style = ByeBooTheme.typography.body2,
                    color = ByeBooTheme.colors.black,
                )
            }
        }
    }
}


