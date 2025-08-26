package com.byeboo.app.presentation.offboarding.navigation

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.byeboo.app.core.navigation.Route
import com.byeboo.app.presentation.offboarding.offboardingcompletedjourney.OffboardingCompletedJourneyRoute
import com.byeboo.app.presentation.offboarding.offboardingnewjourney.OffboardingNewJourneyRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToOffboardingNewJourney(navOptions: NavOptions? = null) {
    navigate(OffboardingNewJourney, navOptions)
}

fun NavController.navigateToOffboardingCompletedJourney(navOptions: NavOptions? = null) {
    navigate(OffboardingCompletedJourney, navOptions)
}

fun NavGraphBuilder.offboardingGraph(
    navigateToQuestStart: () -> Unit,
    navigateToUp: () -> Unit,
    navigateToMyPage: () -> Unit,
    padding: Dp
) {
    composable<OffboardingNewJourney> {
        OffboardingNewJourneyRoute(
            navigateToQuestStart = navigateToQuestStart,
            navigateToUp = navigateToUp,
            bottomPadding = padding
        )
    }

    composable<OffboardingCompletedJourney> {
        OffboardingCompletedJourneyRoute(
            navigateToMyPage = navigateToMyPage,
            bottomPadding = padding
        )
    }
}

@Serializable
data object OffboardingNewJourney : Route

@Serializable
data object OffboardingCompletedJourney : Route