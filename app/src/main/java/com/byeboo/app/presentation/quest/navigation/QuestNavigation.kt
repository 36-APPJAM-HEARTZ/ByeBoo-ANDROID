package com.byeboo.app.presentation.quest.navigation

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.core.util.routeNavigation
import com.byeboo.app.presentation.quest.QuestRoute
import com.byeboo.app.presentation.quest.behavior.navigation.questBehaviorGraph
import com.byeboo.app.presentation.quest.record.navigation.questRecordGraph
import com.byeboo.app.presentation.quest.review.QuestReviewRoute
import com.byeboo.app.presentation.quest.start.QuestStartRoute
import com.byeboo.app.presentation.quest.tip.QuestTipRoute

fun NavController.navigateToQuestStart(
    questType: QuestType? = null,
    navOptions: NavOptions? = null
) {
    navigate(QuestStart(questType), navOptions)
}

fun NavController.navigateToQuest(navOptions: NavOptions? = null) {
    navigate(Quest, navOptions)
}

fun NavController.navigateToQuestTip(
    questId: Long,
    questType: QuestType,
    navOptions: NavOptions? = null
) {
    navigate(QuestTip(questId, questType), navOptions)
}

fun NavController.navigateToQuestReview(questId: Long, navOptions: NavOptions? = null) {
    navigate(QuestReview(questId), navOptions)
}

fun NavGraphBuilder.questGraph(
    navigateUp: () -> Unit,
    navigateToQuest: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToQuestRecording: (Long) -> Unit,
    navigateToQuestBehavior: (Long) -> Unit,
    navigateToQuestReview: (Long) -> Unit,
    navigateToOffboardingCompleteGuide: () -> Unit,
    navigateToQuestRecordingComplete: (Long) -> Unit,
    navigateToQuestRecordingEdit: (Long, Boolean) -> Unit,
    navigateToQuestBehaviorEdit: (Long, Boolean, String) -> Unit,
    navigateToQuestTip: (Long, QuestType) -> Unit,
    navigateToQuestBehaviorComplete: (Long) -> Unit,
    padding: Dp
) {
    routeNavigation<Quest, QuestStart> {
        composable<QuestStart> {
            QuestStartRoute(
                navigateToQuest = navigateToQuest,
                navigateToHome = navigateToHome,
                padding = padding
            )
        }

        composable<Quest> {
            QuestRoute(
                navigateToQuestTip = navigateToQuestTip,
                navigateToQuestRecording = navigateToQuestRecording,
                navigateToQuestBehavior = navigateToQuestBehavior,
                navigateToQuestReview = navigateToQuestReview,
                navigateToOffboardingCompleteGuide = navigateToOffboardingCompleteGuide,
                bottomPadding = padding
            )
        }

        composable<QuestTip> {
            QuestTipRoute(
                navigateToQuest = navigateUp,
                bottomPadding = padding
            )
        }

        composable<QuestReview> {
            QuestReviewRoute(
                navigateToQuest = navigateToQuest,
                navigateToQuestRecordingEdit = navigateToQuestRecordingEdit,
                navigateToQuestBehaviorEdit = navigateToQuestBehaviorEdit,
                bottomPadding = padding
            )
        }

        questRecordGraph(
            navigateToQuest = navigateToQuest,
            navigateToQuestTip = navigateToQuestTip,
            navigateToQuestRecordingComplete = navigateToQuestRecordingComplete,
            navigateToQuestReview = navigateToQuestReview,
            navigateToOffboardingCompletedGuide = navigateToOffboardingCompleteGuide,
            navigateUp = navigateUp,
            bottomPadding = padding
        )

        questBehaviorGraph(
            navigateToQuest = navigateToQuest,
            navigateToQuestTip = navigateToQuestTip,
            navigateToQuestBehaviorComplete = navigateToQuestBehaviorComplete,
            navigateToQuestReview = navigateToQuestReview,
            navigateToOffboardingCompletedGuide = navigateToOffboardingCompleteGuide,
            navigateUp = navigateUp,
            bottomPadding = padding
        )
    }
}
