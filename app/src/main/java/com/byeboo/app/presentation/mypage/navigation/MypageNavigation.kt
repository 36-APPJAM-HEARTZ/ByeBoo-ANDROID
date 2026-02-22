package com.byeboo.app.presentation.mypage.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.byeboo.app.core.navigation.MainTabRoute
import com.byeboo.app.core.navigation.Route
import com.byeboo.app.presentation.mypage.MyPageRoute
import com.byeboo.app.presentation.mypage.blockedusers.BlockedUsersRoute
import com.byeboo.app.presentation.mypage.editprofile.EditProfileRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToMyPage(navOptions: NavOptions? = null) {
    navigate(MyPage, navOptions)
}

fun NavController.navigateToEditProfile(navOptions: NavOptions? = null) {
    navigate(EditProfile, navOptions)
}

fun NavController.navigateToBlockedUsers(navOptions: NavOptions? = null) {
    navigate(BlockedUsers, navOptions)
}

fun NavGraphBuilder.myPageGraph(
    navigateToEditProfile: () -> Unit,
    navigateToOffboardingCompletedJourney: () -> Unit,
    navigateToTutorial: () -> Unit,
    navigateToSplash: () -> Unit,
    navigateToMyPage: () -> Unit,
    navigateToBlockedUsers: () -> Unit,
    navigateUp: () -> Unit,
    paddingValues: PaddingValues,
) {
    composable<MyPage> {
        MyPageRoute(
            navigateToEditProfile = navigateToEditProfile,
            navigateToOffboardingCompletedJourney = navigateToOffboardingCompletedJourney,
            navigateToTutorial = navigateToTutorial,
            navigateToSplash = navigateToSplash,
            navigateToBlockedUsers = navigateToBlockedUsers,
            paddingValues = paddingValues,
        )
    }

    composable<EditProfile> {
        EditProfileRoute(
            navigateToMyPage = navigateToMyPage,
            paddingValues = paddingValues,
        )
    }

    composable<BlockedUsers> {
        BlockedUsersRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
        )
    }
}

@Serializable
data object MyPage : MainTabRoute

@Serializable
data object EditProfile : Route

@Serializable
data object BlockedUsers : Route
