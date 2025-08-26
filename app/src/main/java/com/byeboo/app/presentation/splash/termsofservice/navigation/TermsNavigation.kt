package com.byeboo.app.presentation.splash.termsofservice.navigation

import android.R.attr.padding
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.byeboo.app.core.navigation.Route
import com.byeboo.app.presentation.splash.termsofservice.TermsOfServiceRoute
import kotlinx.serialization.Serializable


fun NavController.navigateToTerms(navOptions: NavOptions? = null) {
    navigate(Terms, navOptions)
}
fun NavGraphBuilder.termsGraph(
    navigateToHome: () -> Unit,
    padding: Dp
) {
    composable<Terms> {
        TermsOfServiceRoute(
            navigateToHome = navigateToHome,
            padding = padding
        )
    }
}

@Serializable
data object Terms : Route