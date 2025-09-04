package com.byeboo.app.presentation.offboarding.navigation

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.byeboo.app.core.navigation.Route
import com.byeboo.app.presentation.offboarding.offboardingcompletedguide.OffboardingCompletedGuideRoute
import com.byeboo.app.presentation.offboarding.offboardingcompletedjourney.OffboardingCompletedJourneyRoute
import com.byeboo.app.presentation.offboarding.offboardingnewjourney.OffboardingNewJourneyRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToOffboardingCompletedGuide(navOptions: NavOptions? = null) {
    navigate(OffboardingCompletedGuide, navOptions)
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
    navigateToOffboardingCompletedJourney: () -> Unit,
    navigateToQuestStart: () -> Unit,
    navigateUp: () -> Unit,
    padding: Dp
) {
    composable<OffboardingCompletedGuide> {
        OffboardingCompletedGuideRoute(
            navigateToHome = navigateToHome,
            navigateToOffboardingNewJourney = navigateToOffboardingNewJourney,
            navigateToOffboardingCompletedJourney = navigateToOffboardingCompletedJourney,
            bottomPadding = padding
        )
    }

    composable<OffboardingNewJourney> {
        OffboardingNewJourneyRoute(
            navigateToQuestStart = navigateToQuestStart,
            navigateUp = navigateUp,
            bottomPadding = padding
        )
    }

    composable<OffboardingCompletedJourney> {
        OffboardingCompletedJourneyRoute(
            navigateUp = navigateUp,
            bottomPadding = padding
        )
    }
}

@Serializable
data object OffboardingCompletedGuide : Route

@Serializable
data object OffboardingNewJourney : Route

@Serializable
data object OffboardingCompletedJourney : Route