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
import com.byeboo.app.presentation.quest.common.navigation.questCommonGraph
import com.byeboo.app.presentation.quest.record.navigation.questRecordGraph
import com.byeboo.app.presentation.quest.review.QuestReviewRoute
import com.byeboo.app.presentation.quest.review.common.all.CommonAnswerRoute
import com.byeboo.app.presentation.quest.review.common.personal.MyAnswerDetailRoute
import com.byeboo.app.presentation.quest.review.common.personal.MyAnswerRoute
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

fun NavController.navigateToQuestMyAnswers(navOptions: NavOptions? = null) {
    navigate(QuestMyAnswers, navOptions)
}

fun NavController.navigateToQuestMyAnswerDetail(
    answerId: Long,
    navOptions: NavOptions? = null,
) {
    navigate(QuestMyAnswersDetail(answerId), navOptions)
}

fun NavGraphBuilder.questGraph(
    navigateUp: () -> Unit,
    navigateToQuest: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToQuestRecording: (Long) -> Unit,
    navigateToQuestBehavior: (Long) -> Unit,
    navigateToQuestCommonWriting: (Long) -> Unit,
    navigateToQuestReview: (Long) -> Unit,
    navigateToOffboardingCompletedGuide: () -> Unit,
    navigateToQuestRecordingComplete: (Long) -> Unit,
    navigateToQuestRecordingEdit: (Long, Boolean) -> Unit,
    navigateToQuestBehaviorEdit: (Long, Boolean, String) -> Unit,
    navigateToQuestTip: (Long, QuestType) -> Unit,
    navigateToQuestBehaviorComplete: (Long) -> Unit,
    navigateToQuestCommonAnswer: (Long) -> Unit,
    navigateToQuestMyAnswers: () -> Unit,
    navigateToQuestMyAnswerDetail: (Long) -> Unit,
    navigateToQuestCommonComplete: (Long) -> Unit,
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
                navigateToQuestCommonWriting = navigateToQuestCommonWriting,
                navigateToQuestReview = navigateToQuestReview,
                navigateToCommonAnswer = navigateToQuestCommonAnswer,
                navigateToQuestMyAnswers = navigateToQuestMyAnswers,
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
                paddingValues = paddingValues,
            )
        }

        composable<QuestMyAnswers> {
            MyAnswerRoute(
                navigateToQuestMyAnswerDetail = navigateToQuestMyAnswerDetail,
                paddingValues = paddingValues,
            )
        }

        composable<QuestMyAnswersDetail> {
            MyAnswerDetailRoute(
                paddingValues = paddingValues,
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

        questCommonGraph(
            navigateToQuest = navigateToQuest,
            navigateToQuestCommonComplete = navigateToQuestCommonComplete,
            navigateUp = navigateUp,
            paddingValues = paddingValues,
        )
    }
}
