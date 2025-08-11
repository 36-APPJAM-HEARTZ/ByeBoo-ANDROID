package com.byeboo.app.presentation.mypage.navigation

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.byeboo.app.core.navigation.MainTabRoute
import com.byeboo.app.presentation.mypage.MyPageRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToMyPage(navOptions: NavOptions? = null) {
    navigate(MyPage, navOptions)
}

fun NavGraphBuilder.myPageGraph(bottomPadding: Dp) {
    composable<MyPage> {
        MyPageRoute(
            bottomPadding = bottomPadding
        )
    }
}

@Serializable
data object MyPage : MainTabRoute
