package com.byeboo.app.presentation.offboarding.navigation

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.core.navigation.Route
import com.byeboo.app.presentation.offboarding.offboardingcompletedguide.OffboardingCompletedGuideRoute
import com.byeboo.app.presentation.offboarding.offboardingcompletedjourney.OffboardingCompletedJourneyRoute
import com.byeboo.app.presentation.offboarding.offboardingnewjourney.OffboardingNewJourneyRoute
import com.byeboo.app.presentation.offboarding.offboardingquestcompleted.OffboardingQuestCompletedRoute
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

fun NavController.navigateToOffboardingQuestCompleted(
    journey: QuestType,
    navOptions: NavOptions? = null
) {
    navigate(OffboardingQuestCompleted(journey), navOptions)
}

fun NavGraphBuilder.offboardingGraph(
    navigateToHome: () -> Unit,
    navigateToOffboardingNewJourney: () -> Unit,
    navigateToOffboardingCompletedJourney: () -> Unit,
    navigateToQuestStart: (QuestType?) -> Unit,
    navigateToQuestReview: (Long) -> Unit,
    navigateUp: () -> Unit,
    navigateToOffboardingQuestCompleted: (QuestType) -> Unit,
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
            navigateToOffboardingQuestCompleted = navigateToOffboardingQuestCompleted,
            bottomPadding = padding
        )
    }

    composable<OffboardingQuestCompleted> { backStackEntry ->
        val offboardingQuestCompleted = backStackEntry.toRoute<OffboardingQuestCompleted>()
        val journey = offboardingQuestCompleted.journey

        OffboardingQuestCompletedRoute(
            journey = journey,
            navigateUp = navigateUp,
            navigateToQuestReview = navigateToQuestReview,
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

@Serializable
data class OffboardingQuestCompleted(val journey: QuestType) : Route
