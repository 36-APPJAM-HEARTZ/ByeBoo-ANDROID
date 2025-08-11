package com.byeboo.app.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.byeboo.app.R
import com.byeboo.app.core.util.screenHeightDp

@Composable
fun SplashRoute(
    navigateToHome: () -> Unit,
    navigateToUserInfo: () -> Unit,
    padding: Dp,
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                SplashState.NavigateToHome -> navigateToHome()
                SplashState.NavigateToUserInfo -> navigateToUserInfo()
            }
        }
    }

    SplashScreen(
        modifier = modifier,
        padding = padding
    )
}

@Composable
fun SplashScreen(
    padding: Dp,
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
                .padding(horizontal = screenHeightDp(76.dp))
                .padding(top = screenHeightDp(padding + 250.dp))
        )
    }
}