package com.byeboo.app.presentation.auth.navigation

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.byeboo.app.core.designsystem.component.backhandler.ByeBooBackHandler
import com.byeboo.app.core.navigation.Route
import com.byeboo.app.presentation.auth.loading.LoadingScreen
import com.byeboo.app.presentation.auth.onboarding.OnboardingRoute
import com.byeboo.app.presentation.auth.userinfo.UserInfoScreen
import kotlinx.serialization.Serializable

fun NavController.navigateToOnboarding(navOptions: NavOptions? = null) {
    navigate(Onboarding, navOptions)
}

fun NavController.navigateToUserInfo(navOptions: NavOptions? = null) {
    navigate(UserInfo, navOptions)
}

fun NavController.navigateToLoading(navOptions: NavOptions? = null) {
    navigate(Loading, navOptions)
}

fun NavGraphBuilder.authGraph(
    navigateToUserInfo: () -> Unit,
    navigateToLoading: () -> Unit,
    navigateToHomeAmulet: () -> Unit,
    padding: Dp
) {
    composable<Onboarding> {
        OnboardingRoute(
            navigateToUserInfo = navigateToUserInfo,
            padding = padding
        )
    }
    composable<UserInfo> {
        UserInfoScreen(
            navigateToLoading = navigateToLoading,
            padding = padding
        )
    }
    composable<Loading> {
        ByeBooBackHandler()
        LoadingScreen(
            navigateToHomeAmulet = navigateToHomeAmulet
        )
    }
}

@Serializable
data object Onboarding : Route

@Serializable
data object UserInfo : Route

@Serializable
data object Loading : Route
