package com.byeboo.app.presentation.main

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.byeboo.app.presentation.auth.navigation.authGraph
import com.byeboo.app.presentation.auth.navigation.navigateToUserInfo
import com.byeboo.app.presentation.home.navigation.Home
import com.byeboo.app.presentation.home.navigation.homeGraph
import com.byeboo.app.presentation.mypage.navigation.myPageGraph
import com.byeboo.app.presentation.quest.behavior.QuestBehaviorViewModel
import com.byeboo.app.presentation.quest.navigation.questGraph
import com.byeboo.app.presentation.splash.navigation.splashGraph
import com.byeboo.app.presentation.splash.termsofservice.navigation.termsGraph

@Composable
fun MainNavHost(
    navigator: MainNavigator,
    padding: Dp,
    modifier: Modifier = Modifier
) {
    val clearStackNavOptions = navOptions {
        popUpTo(0) { inclusive = true }
        launchSingleTop = true
        restoreState = true
    }
    val questNavOptions = navOptions {
        popUpTo(Home) {
            saveState = true
            inclusive = false
        }
        launchSingleTop = true
        restoreState = true
    }
    val questBehaviorViewModel: QuestBehaviorViewModel = hiltViewModel()

    NavHost(
        modifier = modifier,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
        navController = navigator.navController,
        startDestination = navigator.startDestination
    ) {
        splashGraph(
            navigateToHome = { navigator.navigateToHome(clearStackNavOptions) },
            navigateToUserInfo = { navigator.navigateToUserInfo(clearStackNavOptions) },
            navigateToTermsOfService = { navigator.navigateToTerms(clearStackNavOptions )},
            padding = padding
        )
        authGraph(
            navigateToLoading = { navigator.navigateToLoading(clearStackNavOptions) },
            navigateToHomeAmulet = { navigator.navigateToHomeAmulet(clearStackNavOptions) },
            padding = padding
        )
        homeGraph(
            bottomPadding = padding,
            navigateToHome = { navigator.navigateToHome(clearStackNavOptions) },
            navigateToHomeOnboarding = { navigator.navigateToHomeOnboarding(clearStackNavOptions) },
            navigateToQuest = { navigator.navigateToQuest(questNavOptions) },
            navigateToQuestStart = { navigator.navigateToQuestStart(questNavOptions) }

        )
        questGraph(
            navigateToQuest = { navigator.navigateToQuest(clearStackNavOptions) },
            navigateToHome = { navigator.navigateToHome(clearStackNavOptions) },
            navigateToQuestRecording = { questId -> navigator.navigateToQuestRecording(questId) },
            navigateToQuestBehavior = { questId -> navigator.navigateToQuestBehavior(questId) },
            navigateToQuestReview = { questId -> navigator.navigateToQuestReview(questId) },
            navigateToQuestRecordingComplete = { questId ->
                navigator.navigateToQuestRecordingComplete(
                    questId,
                    clearStackNavOptions
                )
            },
            navigateToQuestTip = { questId, questType ->
                navigator.navigateToQuestTip(
                    questId,
                    questType
                )
            },
            navigateToQuestBehaviorComplete = { questId ->
                navigator.navigateToQuestBehaviorComplete(
                    questId,
                    clearStackNavOptions
                )
            },
            navigateUp = navigator::navigateUp,
            viewModel = questBehaviorViewModel,
            padding = padding
        )
        myPageGraph(
            bottomPadding = padding,
            navigateToEditProfile = { navigator.navigateToEditProfile(clearStackNavOptions) },
            navigateToMyPage = { navigator.navigateToMyPage(clearStackNavOptions) }
        )

        termsGraph(
            navigateToUserInfo = { navigator.navigateToUserInfo(clearStackNavOptions) },
            padding = padding
        )
    }
}
