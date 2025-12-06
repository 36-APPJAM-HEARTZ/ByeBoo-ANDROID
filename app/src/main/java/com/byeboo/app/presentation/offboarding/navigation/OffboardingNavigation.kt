package com.byeboo.app.presentation.offboarding.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.core.navigation.Route
import com.byeboo.app.presentation.offboarding.offboardingcompletedguide.OffboardingCompletedGuideRoute
import com.byeboo.app.presentation.offboarding.offboardingcompletedjourney.OffboardingCompletedJourneyRoute
import com.byeboo.app.presentation.offboarding.offboardingnewjourney.OffboardingNewJourneyRoute
import com.byeboo.app.presentation.offboarding.offboardingquestcompleted.OffboardingQuestCompletedRoute
import com.byeboo.app.presentation.offboarding.offboardingquestreview.OffboardingQuestReviewRoute
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
    questType: QuestType,
    navOptions: NavOptions? = null
) {
    navigate(OffboardingQuestCompleted(questType), navOptions)
}

fun NavController.navigateToOffboardingQuestReview(questId: Long, journey: QuestType, navOptions: NavOptions? = null) {
    navigate(OffboardingQuestReview(questId, journey), navOptions)
}

fun NavGraphBuilder.offboardingGraph(
    navigateToHome: () -> Unit,
    navigateToOffboardingNewJourney: () -> Unit,
    navigateToOffboardingCompletedJourney: () -> Unit,
    navigateToQuestStart: (QuestType?) -> Unit,
    navigateToOffboardingQuestReview: (Long, QuestType) -> Unit,
    navigateUp: () -> Unit,
    navigateToOffboardingQuestCompleted: (QuestType) -> Unit,
    navigateToOffboardingQuestCompletedFromReview: (QuestType) -> Unit,
    navigateToQuestRecordingEdit: (Long, Boolean, Boolean) -> Unit,
    navigateToQuestBehaviorEdit: (Long, Boolean, Boolean, String) -> Unit,
    paddingValues: PaddingValues
) {
    composable<OffboardingCompletedGuide> {
        OffboardingCompletedGuideRoute(
            navigateToHome = navigateToHome,
            navigateToOffboardingNewJourney = navigateToOffboardingNewJourney,
            navigateToOffboardingCompletedJourney = navigateToOffboardingCompletedJourney,
            paddingValues = paddingValues
        )
    }

    composable<OffboardingNewJourney> {
        OffboardingNewJourneyRoute(
            navigateToQuestStart = navigateToQuestStart,
            navigateUp = navigateUp,
            paddingValues = paddingValues
        )
    }

    composable<OffboardingCompletedJourney> {
        OffboardingCompletedJourneyRoute(
            navigateUp = navigateUp,
            navigateToOffboardingQuestCompleted = navigateToOffboardingQuestCompleted,
            paddingValues = paddingValues
        )
    }

    composable<OffboardingQuestCompleted> {
        OffboardingQuestCompletedRoute(
            navigateUp = navigateUp,
            navigateToOffboardingQuestReview = navigateToOffboardingQuestReview,
            paddingValues = paddingValues
        )
    }

    composable<OffboardingQuestReview> {
        OffboardingQuestReviewRoute(
            paddingValues = paddingValues,
            navigateToOffboardingQuestCompleted = navigateToOffboardingQuestCompletedFromReview,
            navigateToQuestRecordingEdit = navigateToQuestRecordingEdit,
            navigateToQuestBehaviorEdit = navigateToQuestBehaviorEdit
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
data class OffboardingQuestCompleted(val questType: QuestType) : Route

@Serializable
data class OffboardingQuestReview(val questId: Long, val journeyType: QuestType) : Route
