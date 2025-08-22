package com.byeboo.app.presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.presentation.auth.navigation.navigateToLoading
import com.byeboo.app.presentation.auth.navigation.navigateToUserInfo
import com.byeboo.app.presentation.home.navigation.navigateToHome
import com.byeboo.app.presentation.home.navigation.navigateToHomeAmulet
import com.byeboo.app.presentation.home.navigation.navigateToHomeOnboarding
import com.byeboo.app.presentation.mypage.navigation.MyPage
import com.byeboo.app.presentation.mypage.navigation.navigateToEditProfile
import com.byeboo.app.presentation.mypage.navigation.navigateToMyPage
import com.byeboo.app.presentation.quest.behavior.navigation.navigateToQuestBehavior
import com.byeboo.app.presentation.quest.behavior.navigation.navigateToQuestBehaviorComplete
import com.byeboo.app.presentation.quest.navigation.navigateToQuest
import com.byeboo.app.presentation.quest.navigation.navigateToQuestReview
import com.byeboo.app.presentation.quest.navigation.navigateToQuestStart
import com.byeboo.app.presentation.quest.navigation.navigateToQuestTip
import com.byeboo.app.presentation.quest.record.navigation.navigateToQuestRecording
import com.byeboo.app.presentation.quest.record.navigation.navigateToQuestRecordingComplete
import com.byeboo.app.presentation.splash.navigation.Splash

class MainNavigator(
    val navController: NavHostController
) {
    private val currentDestination: NavDestination?
        @Composable get() =
            navController
                .currentBackStackEntryAsState().value?.destination

    val startDestination = Splash

    val currentTab: MainNavTab?
        @Composable get() =
            MainNavTab.find { tab ->
                currentDestination?.hasRoute(tab::class) == true
            }

    fun navigate(tab: MainNavTab) {
        val navOptions =
            navOptions {
                navController.currentDestination?.route?.let {
                    popUpTo(it) {
                        inclusive = true
                        saveState = true
                    }
                }
                launchSingleTop = true
                restoreState = true
            }
        when (tab) {
            MainNavTab.QUEST -> navController.navigateToQuest(navOptions)
            MainNavTab.HOME -> navController.navigateToHome(navOptions)
            MainNavTab.MYPAGE -> navController.navigateToMyPage(navOptions)
        }
    }

    @Composable
    fun showBottomBar() =
        MainNavTab.contains {
            currentDestination?.hasRoute(it::class) == true
        }

    fun navigateToHome(navOptions: NavOptions) {
        navController.navigateToHome(navOptions)
    }

    fun navigateToMyPage(navOptions: NavOptions? = null) {
        navController.navigateToMyPage(navOptions)
    }

    fun navigateToLoading(navOptions: NavOptions) {
        navController.navigateToLoading(navOptions)
    }

    fun navigateToHomeOnboarding(navOptions: NavOptions) {
        navController.navigateToHomeOnboarding(navOptions)
    }

    fun navigateToUserInfo(navOptions: NavOptions) {
        navController.navigateToUserInfo(navOptions)
    }

    fun navigateUp() {
        navController.navigateUp()
    }

    fun navigateToQuestStart(navOptions: NavOptions) {
        navController.navigateToQuestStart(navOptions)
    }

    fun navigateToQuest(navOptions: NavOptions) {
        navController.navigateToQuest(navOptions)
    }

    fun navigateToQuestTip(questId: Long, questType: QuestType, navOptions: NavOptions? = null) {
        navController.navigateToQuestTip(questId, questType, navOptions)
    }

    fun navigateToQuestRecording(questId: Long, navOptions: NavOptions? = null) {
        navController.navigateToQuestRecording(questId, navOptions)
    }

    fun navigateToQuestBehavior(questId: Long, navOptions: NavOptions? = null) {
        navController.navigateToQuestBehavior(questId, navOptions)
    }

    fun navigateToQuestRecordingComplete(questId: Long, navOptions: NavOptions? = null) {
        navController.navigateToQuestRecordingComplete(questId, navOptions)
    }

    fun navigateToQuestBehaviorComplete(questId: Long, navOptions: NavOptions? = null) {
        navController.navigateToQuestBehaviorComplete(questId, navOptions)
    }

    fun navigateToQuestReview(questId: Long, navOptions: NavOptions? = null) {
        navController.navigateToQuestReview(questId, navOptions)
    }

    fun navigateToHomeAmulet(navOptions: NavOptions) {
        navController.navigateToHomeAmulet(navOptions)
    }

    fun navigateToEditProfile(navOptions: NavOptions) {
        navController.navigateToEditProfile(navOptions)
    }
}

@Composable
fun rememberMainNavigator(
    navController: NavHostController = rememberNavController()
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}
