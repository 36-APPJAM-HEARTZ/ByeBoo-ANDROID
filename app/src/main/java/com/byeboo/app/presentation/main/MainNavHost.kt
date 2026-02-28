package com.byeboo.app.presentation.main

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.byeboo.app.presentation.auth.navigation.authGraph
import com.byeboo.app.presentation.home.navigation.Home
import com.byeboo.app.presentation.home.navigation.homeGraph
import com.byeboo.app.presentation.mypage.navigation.myPageGraph
import com.byeboo.app.presentation.offboarding.navigation.offboardingGraph
import com.byeboo.app.presentation.quest.navigation.questGraph
import com.byeboo.app.presentation.splash.navigation.splashGraph
import com.byeboo.app.presentation.tutorial.navigation.tutorialGraph

@Composable
fun MainNavHost(
    navigator: MainNavigator,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val clearStackNavOptions =
        navOptions {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
            restoreState = false
        }
    val questNavOptions =
        navOptions {
            popUpTo(Home) {
                saveState = true
                inclusive = false
            }
            launchSingleTop = true
            restoreState = true
        }
    val keepStackNavOptions =
        navOptions {
            launchSingleTop = true
            restoreState = true
        }

    NavHost(
        modifier = modifier,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
        navController = navigator.navController,
        startDestination = navigator.startDestination,
    ) {
        splashGraph(
            navigateToHome = { navigator.navigateToHome(clearStackNavOptions) },
            navigateToUserInfo = { navigator.navigateToUserInfo(clearStackNavOptions) },
            navigateToTermsOfService = { navigator.navigateToTerms(clearStackNavOptions) },
            paddingValues = paddingValues,
        )

        authGraph(
            navigateToLoading = { navigator.navigateToLoading(clearStackNavOptions) },
            navigateToHomeAmulet = { navigator.navigateToHomeAmulet(clearStackNavOptions) },
            paddingValues = paddingValues,
        )

        homeGraph(
            navigateToQuest = { navigator.navigateToQuest(questNavOptions) },
            navigateToQuestStart = { journey ->
                navigator.navigateToQuestStart(
                    questType = journey,
                    navOptions = questNavOptions,
                )
            },
            navigateToTutorial = { navigator.navigateToTutorial(keepStackNavOptions) },
            navigateToOffboardingCompletedGuide = {
                navigator.navigateToOffboardingCompletedGuide(
                    clearStackNavOptions,
                )
            },
            navigateToOffboardingNewJourney = {
                navigator.navigateToOffboardingNewJourney(
                    keepStackNavOptions,
                )
            },
            navigateToHome = { navigator.navigateToHome(clearStackNavOptions) },
            navigateToHomeOnboarding = { navigator.navigateToHomeOnboarding(clearStackNavOptions) },
            paddingValues = paddingValues,
        )

        questGraph(
            navigateToQuest = { navigator.navigateToQuest(clearStackNavOptions) },
            navigateToHome = { navigator.navigateToHome(questNavOptions) },
            navigateToQuestRecording = { questId -> navigator.navigateToQuestRecording(questId) },
            navigateToQuestBehavior = { questId -> navigator.navigateToQuestBehavior(questId) },
            navigateToQuestCommon = { questId -> navigator.navigateToQuestCommon(questId) },
            navigateToQuestReview = { questId -> navigator.navigateToQuestReview(questId) },
            navigateToOffboardingCompletedGuide = {
                navigator.navigateToOffboardingCompletedGuide(
                    clearStackNavOptions,
                )
            },
            navigateToQuestRecordingComplete = { questId ->
                navigator.navigateToQuestRecordingComplete(
                    questId = questId,
                    navOptions = clearStackNavOptions,
                )
            },
            navigateToQuestRecordingEdit = { questId, isEditMode ->
                navigator.navigateToQuestRecording(
                    questId = questId,
                    isEditMode = isEditMode,
                    navOptions = keepStackNavOptions,
                )
            },
            navigateToQuestBehaviorEdit = { questId, isEditMode, imageKey ->
                navigator.navigateToQuestBehavior(
                    questId = questId,
                    isEditMode = isEditMode,
                    imageKey = imageKey,
                    navOptions = keepStackNavOptions,
                )
            },
            navigateToQuestTip = { questId, questType ->
                navigator.navigateToQuestTip(
                    questId = questId,
                    questType = questType,
                )
            },
            navigateToQuestBehaviorComplete = { questId ->
                navigator.navigateToQuestBehaviorComplete(
                    questId = questId,
                    navOptions = clearStackNavOptions,
                )
            },
            navigateToQuestCommonComplete = { questId ->
                navigator.navigateToQuestCommonComplete(
                    questId = questId,
                    navOptions = clearStackNavOptions,
                )
            },
            navigateUp = navigator::navigateUp,
            paddingValues = paddingValues,
        )

        myPageGraph(
            navigateToEditProfile = { navigator.navigateToEditProfile(keepStackNavOptions) },
            navigateToOffboardingCompletedJourney = {
                navigator.navigateToOffboardingCompletedJourney(
                    keepStackNavOptions,
                )
            },
            navigateToTutorial = { navigator.navigateToTutorial(keepStackNavOptions) },
            navigateToMyPage = { navigator.navigateToMyPage(clearStackNavOptions) },
            navigateToBlockedUsers = { navigator.navigateToBlockedUsers(keepStackNavOptions) },
            navigateToSplash = { navigator.navigateToSplash(clearStackNavOptions) },
            navigateUp = navigator::navigateUp,
            paddingValues = paddingValues,
        )

        offboardingGraph(
            navigateToHome = { navigator.navigateToHome(questNavOptions) },
            navigateToOffboardingNewJourney = {
                navigator.navigateToOffboardingNewJourney(
                    keepStackNavOptions,
                )
            },
            navigateToOffboardingCompletedJourney = {
                navigator.navigateToOffboardingCompletedJourney(
                    keepStackNavOptions,
                )
            },
            navigateToQuestStart = { journey ->
                navigator.navigateToQuestStart(
                    questType = journey,
                    navOptions = keepStackNavOptions,
                )
            },
            navigateToOffboardingQuestReview = { questId, journey ->
                navigator.navigateToOffboardingQuestReview(
                    questId,
                    journey,
                )
            },
            navigateUp = navigator::navigateUp,
            navigateToOffboardingQuestCompleted = { journey ->
                navigator.navigateToOffboardingQuestCompleted(
                    questType = journey,
                    navOptions = keepStackNavOptions,
                )
            },
            navigateToOffboardingQuestCompletedFromReview = { journey ->
                navigator.navigateToOffboardingQuestCompletedFromReview(journey)
            },
            navigateToQuestRecordingEdit = { questId, isEditMode, fromOffboarding ->
                navigator.navigateToQuestRecording(
                    questId = questId,
                    isEditMode = isEditMode,
                    fromOffboarding = fromOffboarding,
                    navOptions = keepStackNavOptions,
                )
            },
            navigateToQuestBehaviorEdit = { questId, isEditMode, fromOffboarding, imageKey ->
                navigator.navigateToQuestBehavior(
                    questId = questId,
                    isEditMode = isEditMode,
                    fromOffboarding = fromOffboarding,
                    imageKey = imageKey,
                    navOptions = keepStackNavOptions,
                )
            },
            paddingValues = paddingValues,
        )

        tutorialGraph(
            navigateToUp = navigator::navigateUp,
            paddingValues = paddingValues,
        )
    }
}
