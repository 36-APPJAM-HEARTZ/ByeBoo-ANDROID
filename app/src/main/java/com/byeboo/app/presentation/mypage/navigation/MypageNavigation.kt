package com.byeboo.app.presentation.mypage.navigation

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.byeboo.app.core.navigation.MainTabRoute
import com.byeboo.app.core.navigation.Route
import com.byeboo.app.presentation.mypage.MyPageRoute
import com.byeboo.app.presentation.mypage.editprofile.EditProfileRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToMyPage(navOptions: NavOptions? = null) {
    navigate(MyPage, navOptions)
}

fun NavController.navigateToEditProfile(navOptions: NavOptions? = null) {
    navigate(EditProfile, navOptions)
}


fun NavGraphBuilder.myPageGraph(
    navigateToEditProfile: () -> Unit,
    navigateToOffboardingCompletedJourney: () -> Unit,
    navigateToTutorial: () -> Unit,
    navigateToSplash: () -> Unit,
    navigateToMyPage: () -> Unit,
    padding: Dp,
) {
    composable<MyPage> {
        MyPageRoute(
            navigateToEditProfile = navigateToEditProfile,
            navigateToOffboardingCompletedJourney = navigateToOffboardingCompletedJourney,
            navigateToTutorial = navigateToTutorial,
            navigateToSplash = navigateToSplash,
            bottomPadding = padding
        )
    }

    composable<EditProfile> {
        EditProfileRoute(
            navigateToMyPage = navigateToMyPage,
            bottomPadding = padding
        )
    }
}

@Serializable
data object MyPage : MainTabRoute

@Serializable
data object EditProfile : Route
