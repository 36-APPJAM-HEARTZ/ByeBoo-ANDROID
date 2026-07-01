package com.byeboo.app.presentation.notification.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.byeboo.app.core.navigation.Route
import com.byeboo.app.presentation.notification.NotificationRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToNotificationList(navOptions: NavOptions) {
    navigate(NotificationList, navOptions)
}

fun NavGraphBuilder.notificationGraph(
    navigateToHome: () -> Unit,
    navigateToDeepLink: (String) -> Unit,
    paddingValues: PaddingValues,
) {
    composable<NotificationList> {
        NotificationRoute(
            navigateToHome = navigateToHome,
            navigateToDeepLink = navigateToDeepLink,
            paddingValues = paddingValues,
        )
    }
}

@Serializable
data object NotificationList : Route
