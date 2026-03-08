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
import com.byeboo.app.presentation.mypage.navigation.navigateToBlockedUsers
import com.byeboo.app.presentation.mypage.navigation.navigateToEditProfile
import com.byeboo.app.presentation.mypage.navigation.navigateToMyPage
import com.byeboo.app.presentation.offboarding.navigation.OffboardingQuestCompleted
import com.byeboo.app.presentation.offboarding.navigation.navigateToOffboardingCompletedGuide
import com.byeboo.app.presentation.offboarding.navigation.navigateToOffboardingCompletedJourney
import com.byeboo.app.presentation.offboarding.navigation.navigateToOffboardingNewJourney
import com.byeboo.app.presentation.offboarding.navigation.navigateToOffboardingQuestCompleted
import com.byeboo.app.presentation.offboarding.navigation.navigateToOffboardingQuestReview
import com.byeboo.app.presentation.quest.behavior.navigation.navigateToQuestBehavior
import com.byeboo.app.presentation.quest.behavior.navigation.navigateToQuestBehaviorComplete
import com.byeboo.app.presentation.quest.common.navigation.navigateToQuestCommonWriting
import com.byeboo.app.presentation.quest.navigation.AiAnswerOrigin
import com.byeboo.app.presentation.quest.navigation.Quest
import com.byeboo.app.presentation.quest.navigation.QuestResultKey
import com.byeboo.app.presentation.quest.navigation.navigateToQuest
import com.byeboo.app.presentation.quest.navigation.navigateToQuestAiAnswer
import com.byeboo.app.presentation.quest.navigation.navigateToQuestCommonAnswer
import com.byeboo.app.presentation.quest.navigation.navigateToQuestMyAnswerDetail
import com.byeboo.app.presentation.quest.navigation.navigateToQuestMyAnswers
import com.byeboo.app.presentation.quest.navigation.navigateToQuestReview
import com.byeboo.app.presentation.quest.navigation.navigateToQuestStart
import com.byeboo.app.presentation.quest.navigation.navigateToQuestTip
import com.byeboo.app.presentation.quest.record.navigation.navigateToQuestRecording
import com.byeboo.app.presentation.quest.record.navigation.navigateToQuestRecordingComplete
import com.byeboo.app.presentation.splash.navigation.Splash
import com.byeboo.app.presentation.splash.navigation.navigateToSplash
import com.byeboo.app.presentation.splash.navigation.navigateToTerms
import com.byeboo.app.presentation.tutorial.navigation.navigateToTutorial

class MainNavigator(
    val navController: NavHostController,
) {
    private val currentDestination: NavDestination?
        @Composable get() =
            navController
                .currentBackStackEntryAsState()
                .value
                ?.destination

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
        val myPageOptions =
            navOptions {
                navController.currentDestination?.route?.let {
                    popUpTo(it) {
                        inclusive = true
                        saveState = true
                    }
                }
                launchSingleTop = true
                restoreState = false
            }

        when (tab) {
            MainNavTab.QUEST -> navController.navigateToQuest(navOptions)
            MainNavTab.HOME -> navController.navigateToHome(navOptions)
            MainNavTab.MYPAGE -> navController.navigateToMyPage(myPageOptions)
        }
    }

    fun navigateToOffboardingQuestCompletedFromReview(questType: QuestType) {
        navController.navigate(OffboardingQuestCompleted(questType)) {
            popUpTo(OffboardingQuestCompleted(questType)) {
                inclusive = true
            }
            launchSingleTop = true
            restoreState = false
        }
    }

    @Composable
    fun showBottomBar() =
        MainNavTab.contains {
            currentDestination?.hasRoute(it::class) == true
        }

    fun navigateUp() {
        navController.navigateUp()
    }

    fun navigateToTerms(navOptions: NavOptions) {
        navController.navigateToTerms(navOptions)
    }

    fun navigateToHomeAmulet(navOptions: NavOptions) {
        navController.navigateToHomeAmulet(navOptions)
    }

    fun navigateToHome(navOptions: NavOptions) {
        navController.navigateToHome(navOptions)
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

    fun navigateToQuestStart(
        questType: QuestType? = null,
        navOptions: NavOptions,
    ) {
        navController.navigateToQuestStart(questType = questType, navOptions = navOptions)
    }

    fun navigateToQuest(options: NavOptions) {
        navController.navigate(route = Quest, navOptions = options)
    }

    fun navigateToQuestFromComplete() {
        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.set(QuestResultKey.COMMON_COMPLETED, true)

        navController.popBackStack()
    }

    fun navigateToQuestTip(
        questId: Long,
        questType: QuestType,
        navOptions: NavOptions? = null,
    ) {
        navController.navigateToQuestTip(
            questId = questId,
            questType = questType,
            navOptions = navOptions,
        )
    }

    fun navigateToQuestRecording(
        questId: Long,
        isEditMode: Boolean = false,
        fromOffboarding: Boolean = false,
        navOptions: NavOptions? = null,
    ) {
        navController.navigateToQuestRecording(
            questId = questId,
            isEditMode = isEditMode,
            fromOffboarding = fromOffboarding,
            navOptions = navOptions,
        )
    }

    fun navigateToQuestBehavior(
        questId: Long,
        isEditMode: Boolean = false,
        fromOffboarding: Boolean = false,
        imageKey: String? = null,
        navOptions: NavOptions? = null,
    ) {
        navController.navigateToQuestBehavior(
            questId = questId,
            isEditMode = isEditMode,
            imageKey = imageKey,
            fromOffboarding = fromOffboarding,
            navOptions = navOptions,
        )
    }

    fun navigateToQuestCommonWriting(
        questId: Long? = 0,
        answerId: Long? = 0,
        isEditMode: Boolean = false,
        navOptions: NavOptions? = null,
    ) {
        navController.navigateToQuestCommonWriting(
            questId = questId,
            answerId = answerId,
            isEditMode = isEditMode,
            navOptions = navOptions,
        )
    }

    fun navigateToQuestRecordingComplete(
        questId: Long,
        navOptions: NavOptions? = null,
    ) {
        navController.navigateToQuestRecordingComplete(questId = questId, navOptions = navOptions)
    }

    fun navigateToQuestBehaviorComplete(
        questId: Long,
        navOptions: NavOptions? = null,
    ) {
        navController.navigateToQuestBehaviorComplete(questId = questId, navOptions = navOptions)
    }

    fun navigateToQuestCommonAnswer(
        answerId: Long,
        navOptions: NavOptions? = null,
    ) {
        navController.navigateToQuestCommonAnswer(answerId = answerId, navOptions = navOptions)
    }

    fun navigateToQuestMyAnswers(navOptions: NavOptions? = null) {
        navController.navigateToQuestMyAnswers(navOptions = navOptions)
    }

    fun navigateToQuestMyAnswerDetail(
        answerId: Long,
        navOptions: NavOptions? = null,
    ) {
        navController.navigateToQuestMyAnswerDetail(answerId = answerId, navOptions = navOptions)
    }

    fun navigateToQuestReview(
        questId: Long,
        navOptions: NavOptions? = null,
    ) {
        navController.navigateToQuestReview(questId = questId, navOptions = navOptions)
    }

    fun navigateToQuestAiAnswer(
        questId: Long,
        isExistedAiAnswer: Boolean,
        aiAnswerOrigin: AiAnswerOrigin,
        navOptions: NavOptions? = null,
    ) {
        navController.navigateToQuestAiAnswer(
            questId = questId,
            isExistedAiAnswer = isExistedAiAnswer,
            aiAnswerOrigin = aiAnswerOrigin,
            navOptions = navOptions,
        )
    }

    fun navigateToMyPage(navOptions: NavOptions? = null) {
        navController.navigateToMyPage(navOptions)
    }

    fun navigateToEditProfile(navOptions: NavOptions) {
        navController.navigateToEditProfile(navOptions)
    }

    fun navigateToBlockedUsers(navOptions: NavOptions) {
        navController.navigateToBlockedUsers(navOptions)
    }

    fun navigateToTutorial(navOptions: NavOptions) {
        navController.navigateToTutorial(navOptions)
    }

    fun navigateToOffboardingCompletedGuide(navOptions: NavOptions) {
        navController.navigateToOffboardingCompletedGuide(navOptions)
    }

    fun navigateToOffboardingNewJourney(navOptions: NavOptions) {
        navController.navigateToOffboardingNewJourney(navOptions)
    }

    fun navigateToOffboardingCompletedJourney(navOptions: NavOptions) {
        navController.navigateToOffboardingCompletedJourney(navOptions)
    }

    fun navigateToOffboardingQuestCompleted(
        questType: QuestType,
        navOptions: NavOptions,
    ) {
        navController.navigateToOffboardingQuestCompleted(
            questType = questType,
            navOptions = navOptions,
        )
    }

    fun navigateToOffboardingQuestReview(
        questId: Long,
        journey: QuestType,
    ) {
        navController.navigateToOffboardingQuestReview(questId = questId, journey = journey)
    }

    fun navigateToSplash(navOptions: NavOptions) {
        navController.navigateToSplash(navOptions)
    }
}

@Composable
fun rememberMainNavigator(navController: NavHostController = rememberNavController()): MainNavigator =
    remember(navController) {
        MainNavigator(navController)
    }
