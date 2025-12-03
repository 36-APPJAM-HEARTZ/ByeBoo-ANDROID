package com.byeboo.app.presentation.auth.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.byeboo.app.core.navigation.Route
import com.byeboo.app.presentation.auth.loading.LoadingRoute
import com.byeboo.app.presentation.auth.userinfo.UserInfoRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToUserInfo(navOptions: NavOptions? = null) {
    navigate(UserInfo, navOptions)
}

fun NavController.navigateToLoading(navOptions: NavOptions? = null) {
    navigate(Loading, navOptions)
}

fun NavGraphBuilder.authGraph(
    navigateToLoading: () -> Unit,
    navigateToHomeAmulet: () -> Unit,
    paddingValues: PaddingValues
) {
    composable<UserInfo> {
        UserInfoRoute(
            navigateToLoading = navigateToLoading,
            paddingValues = paddingValues
        )
    }

    composable<Loading> {
        LoadingRoute(
            navigateToHomeAmulet = navigateToHomeAmulet
        )
    }
}

@Serializable
data object UserInfo : Route

@Serializable
data object Loading : Route
