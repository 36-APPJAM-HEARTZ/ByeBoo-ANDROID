package com.byeboo.app.presentation.splash.termsofservice.navigation

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.byeboo.app.core.designsystem.component.backhandler.ByeBooBackHandler
import com.byeboo.app.core.navigation.Route
import com.byeboo.app.presentation.splash.termsofservice.TermsOfServiceRoute
import kotlinx.serialization.Serializable


fun NavController.navigateToTerms(navOptions: NavOptions? = null) {
    navigate(Terms, navOptions)
}
fun NavGraphBuilder.termsGraph(
    navigateToUserInfo: () -> Unit,
    padding: Dp
) {
    composable<Terms> {
        ByeBooBackHandler()
        TermsOfServiceRoute(
            navigateToUserInfo = navigateToUserInfo,
            padding = padding
        )
    }
}

@Serializable
data object Terms : Route