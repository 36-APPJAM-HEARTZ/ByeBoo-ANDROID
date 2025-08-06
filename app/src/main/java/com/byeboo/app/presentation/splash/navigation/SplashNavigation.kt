package com.byeboo.app.presentation.splash.navigation

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.byeboo.app.core.navigation.Route
import com.byeboo.app.presentation.splash.SplashRoute
import kotlinx.serialization.Serializable

fun NavGraphBuilder.splashGraph(
    navigateToHome: () -> Unit,
    navigateToOnboarding: () -> Unit,
    padding: Dp
) {
    composable<Splash> {
        SplashRoute(
            navigateToHome = navigateToHome,
            navigateToOnboarding = navigateToOnboarding,
            padding = padding
        )
    }
}

@Serializable
data object Splash : Route
