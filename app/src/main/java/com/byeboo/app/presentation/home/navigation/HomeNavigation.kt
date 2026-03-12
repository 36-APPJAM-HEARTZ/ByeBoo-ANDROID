package com.byeboo.app.presentation.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.byeboo.app.core.designsystem.component.backhandler.ByeBooBackHandler
import com.byeboo.app.core.model.quest.JourneyType
import com.byeboo.app.core.navigation.MainTabRoute
import com.byeboo.app.core.navigation.Route
import com.byeboo.app.presentation.home.HomeRoute
import com.byeboo.app.presentation.home.homeamulet.HomeAmuletRoute
import com.byeboo.app.presentation.home.homeonboarding.HomeOnboardingRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    navigate(Home, navOptions)
}

fun NavController.navigateToHomeOnboarding(navOptions: NavOptions? = null) {
    navigate(HomeOnboarding, navOptions)
}

fun NavController.navigateToHomeAmulet(navOptions: NavOptions? = null) {
    navigate(HomeAmulet, navOptions)
}

fun NavGraphBuilder.homeGraph(
    navigateToQuest: () -> Unit,
    navigateToQuestStart: (JourneyType?) -> Unit,
    navigateToTutorial: () -> Unit,
    navigateToOffboardingCompletedGuide: () -> Unit,
    navigateToOffboardingNewJourney: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToHomeOnboarding: () -> Unit,
    paddingValues: PaddingValues,
) {
    composable<Home> {
        HomeRoute(
            navigateToQuest = navigateToQuest,
            navigateToQuestStart = navigateToQuestStart,
            navigateToTutorial = navigateToTutorial,
            navigateToOffboardingCompletedGuide = navigateToOffboardingCompletedGuide,
            navigateToOffboardingNewJourney = navigateToOffboardingNewJourney,
            paddingValues = paddingValues,
        )
    }
    composable<HomeOnboarding> {
        ByeBooBackHandler()
        HomeOnboardingRoute(
            navigateToHome = navigateToHome,
            paddingValues = paddingValues,
        )
    }
    composable<HomeAmulet> {
        ByeBooBackHandler()
        HomeAmuletRoute(
            navigateToHomeOnboarding = navigateToHomeOnboarding,
        )
    }
}

@Serializable
data object Home : MainTabRoute

@Serializable
data object HomeOnboarding : Route

@Serializable
data object HomeAmulet : Route
