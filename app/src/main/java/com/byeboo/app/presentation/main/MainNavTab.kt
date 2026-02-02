package com.byeboo.app.presentation.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import com.byeboo.app.R.drawable.ic_home
import com.byeboo.app.R.drawable.ic_quest
import com.byeboo.app.R.drawable.ic_user
import com.byeboo.app.R.string.ic_home_desc
import com.byeboo.app.R.string.ic_mypage_desc
import com.byeboo.app.R.string.ic_quest_desc
import com.byeboo.app.core.navigation.MainTabRoute
import com.byeboo.app.core.navigation.Route
import com.byeboo.app.presentation.home.navigation.Home
import com.byeboo.app.presentation.mypage.navigation.MyPage
import com.byeboo.app.presentation.quest.navigation.Quest

enum class MainNavTab(
    @DrawableRes val icon: Int,
    @StringRes val contentDescription: Int,
    val route: MainTabRoute
) {
    HOME(
        icon = ic_home,
        contentDescription = ic_home_desc,
        route = Home
    ),
    QUEST(
        icon = ic_quest,
        contentDescription = ic_quest_desc,
        route = Quest
    ),
    MYPAGE(
        icon = ic_user,
        contentDescription = ic_mypage_desc,
        route = MyPage
    )
    ;

    companion object {
        @Composable
        fun find(predicate: @Composable (MainTabRoute) -> Boolean): MainNavTab? = entries.find {
            predicate(
                it.route
            )
        }

        @Composable
        fun contains(predicate: @Composable (Route) -> Boolean): Boolean = entries.map { it.route }.any {
            predicate(
                it
            )
        }
    }
}
