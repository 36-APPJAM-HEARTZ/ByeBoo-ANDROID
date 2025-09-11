package com.byeboo.app.presentation.splash.navigation

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.byeboo.app.core.designsystem.component.backhandler.ByeBooBackHandler
import com.byeboo.app.core.navigation.Route
import com.byeboo.app.presentation.splash.SplashRoute
import com.byeboo.app.presentation.splash.termsofservice.TermsOfServiceRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToSplash(navOptions: NavOptions) {
    navigate(Splash, navOptions)
}

fun NavController.navigateToTerms(navOptions: NavOptions? = null) {
    navigate(Terms, navOptions)
}

fun NavGraphBuilder.splashGraph(
    navigateToHome: () -> Unit,
    navigateToUserInfo: () -> Unit,
    navigateToTermsOfService: () -> Unit,
    padding: Dp
) {
    composable<Splash> {
        SplashRoute(
            navigateToHome = navigateToHome,
            navigateToUserInfo = navigateToUserInfo,
            navigateToTermsOfService = navigateToTermsOfService,
            padding = padding
        )
    }
    composable<Terms> {
        ByeBooBackHandler()
        TermsOfServiceRoute(
            navigateToUserInfo = navigateToUserInfo,
            padding = padding
        )
    }
}

@Serializable
data object Splash : Route

@Serializable
data object Terms : Route

