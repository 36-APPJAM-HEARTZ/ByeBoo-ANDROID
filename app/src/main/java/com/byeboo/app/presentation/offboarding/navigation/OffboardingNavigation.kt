package com.byeboo.app.presentation.offboarding.navigation

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.byeboo.app.core.navigation.Route
import com.byeboo.app.presentation.offboarding.offboardingcompletedjourney.OffboardingCompletedJourneyRoute
import com.byeboo.app.presentation.offboarding.offboardingcompleteguide.OffboardingCompleteGuideRoute
import com.byeboo.app.presentation.offboarding.offboardingnewjourney.OffboardingNewJourneyRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToOffboardingCompleteGuide(navOptions: NavOptions? = null) {
    navigate(OffboardingCompleteGuide, navOptions)
}

fun NavController.navigateToOffboardingNewJourney(navOptions: NavOptions? = null) {
    navigate(OffboardingNewJourney, navOptions)
}

fun NavController.navigateToOffboardingCompletedJourney(navOptions: NavOptions? = null) {
    navigate(OffboardingCompletedJourney, navOptions)
}

fun NavGraphBuilder.offboardingGraph(
    navigateToHome: () -> Unit,
    navigateToOffboardingNewJourney: () -> Unit,
    navigateToOffboardingCompleteJourney: () -> Unit,
    navigateToQuestStart: () -> Unit,
    navigateToUp: () -> Unit,
    padding: Dp
) {
    composable<OffboardingCompleteGuide> {
        OffboardingCompleteGuideRoute(
            navigateToHome = navigateToHome,
            navigateToOffboardingNewJourney = navigateToOffboardingNewJourney,
            navigateToOffboardingCompleteJourney = navigateToOffboardingCompleteJourney,
            bottomPadding = padding
        )
    }

    composable<OffboardingNewJourney> {
        OffboardingNewJourneyRoute(
            navigateToQuestStart = navigateToQuestStart,
            navigateToUp = navigateToUp,
            bottomPadding = padding
        )
    }

    composable<OffboardingCompletedJourney> {
        OffboardingCompletedJourneyRoute(
            navigateToUp = navigateToUp,
            bottomPadding = padding
        )
    }
}

@Serializable
data object OffboardingCompleteGuide : Route

@Serializable
data object OffboardingNewJourney : Route

@Serializable
data object OffboardingCompletedJourney : Route