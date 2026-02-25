package com.byeboo.app.presentation.quest.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.core.util.routeNavigation
import com.byeboo.app.presentation.quest.QuestRoute
import com.byeboo.app.presentation.quest.behavior.navigation.questBehaviorGraph
import com.byeboo.app.presentation.quest.record.navigation.questRecordGraph
import com.byeboo.app.presentation.quest.review.common.CommonAnswerRoute
import com.byeboo.app.presentation.quest.review.my.QuestReviewRoute
import com.byeboo.app.presentation.quest.start.QuestStartRoute
import com.byeboo.app.presentation.quest.tip.QuestTipRoute

fun NavController.navigateToQuestStart(
    questType: QuestType? = null,
    navOptions: NavOptions? = null,
) {
    navigate(QuestStart(questType), navOptions)
}

fun NavController.navigateToQuest(navOptions: NavOptions? = null) {
    navigate(Quest, navOptions)
}

fun NavController.navigateToQuestTip(
    questId: Long,
    questType: QuestType,
    navOptions: NavOptions? = null,
) {
    navigate(QuestTip(questId, questType), navOptions)
}

fun NavController.navigateToQuestReview(
    questId: Long,
    navOptions: NavOptions? = null,
) {
    navigate(QuestReview(questId), navOptions)
}

fun NavController.navigateToQuestCommonAnswer(
    answerId: Long,
    navOptions: NavOptions? = null,
) {
    navigate(QuestCommonAnswer(answerId), navOptions)
}

fun NavGraphBuilder.questGraph(
    navigateUp: () -> Unit,
    navigateToQuest: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToQuestRecording: (Long) -> Unit,
    navigateToQuestBehavior: (Long) -> Unit,
    navigateToQuestReview: (Long) -> Unit,
    navigateToOffboardingCompletedGuide: () -> Unit,
    navigateToQuestRecordingComplete: (Long) -> Unit,
    navigateToQuestRecordingEdit: (Long, Boolean) -> Unit,
    navigateToQuestBehaviorEdit: (Long, Boolean, String) -> Unit,
    navigateToQuestTip: (Long, QuestType) -> Unit,
    navigateToQuestBehaviorComplete: (Long) -> Unit,
    navigateToQuestCommonAnswer: (Long) -> Unit,
    paddingValues: PaddingValues,
) {
    routeNavigation<Quest, QuestStart> {
        composable<QuestStart> {
            QuestStartRoute(
                navigateToQuest = navigateToQuest,
                navigateToHome = navigateToHome,
                paddingValues = paddingValues,
            )
        }

        composable<Quest> {
            QuestRoute(
                navigateToQuestTip = navigateToQuestTip,
                navigateToQuestRecording = navigateToQuestRecording,
                navigateToQuestBehavior = navigateToQuestBehavior,
                navigateToQuestReview = navigateToQuestReview,
                navigateToCommonAnswer = navigateToQuestCommonAnswer,
                paddingValues = paddingValues,
            )
        }

        composable<QuestTip> {
            QuestTipRoute(
                navigateToQuest = navigateUp,
                paddingValues = paddingValues,
            )
        }

        composable<QuestReview> {
            QuestReviewRoute(
                navigateToQuest = navigateToQuest,
                navigateToQuestRecordingEdit = navigateToQuestRecordingEdit,
                navigateToQuestBehaviorEdit = navigateToQuestBehaviorEdit,
                paddingValues = paddingValues,
            )
        }

        composable<QuestCommonAnswer> {
            CommonAnswerRoute(
                navigateToQuest = navigateToQuest,
                paddingValues = paddingValues
            )
        }

        questRecordGraph(
            navigateToQuest = navigateToQuest,
            navigateToQuestTip = navigateToQuestTip,
            navigateToQuestRecordingComplete = navigateToQuestRecordingComplete,
            navigateToQuestReview = navigateToQuestReview,
            navigateToOffboardingCompletedGuide = navigateToOffboardingCompletedGuide,
            navigateUp = navigateUp,
            paddingValues = paddingValues,
        )

        questBehaviorGraph(
            navigateToQuest = navigateToQuest,
            navigateToQuestTip = navigateToQuestTip,
            navigateToQuestBehaviorComplete = navigateToQuestBehaviorComplete,
            navigateToQuestReview = navigateToQuestReview,
            navigateToOffboardingCompletedGuide = navigateToOffboardingCompletedGuide,
            navigateUp = navigateUp,
            paddingValues = paddingValues,
        )
    }
}
