package com.byeboo.app.presentation.quest.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.core.util.routeNavigation
import com.byeboo.app.presentation.quest.QuestRoute
import com.byeboo.app.presentation.quest.aianswer.QuestAiAnswerRoute
import com.byeboo.app.presentation.quest.behavior.navigation.questBehaviorGraph
import com.byeboo.app.presentation.quest.common.navigation.questCommonGraph
import com.byeboo.app.presentation.quest.record.navigation.questRecordGraph
import com.byeboo.app.presentation.quest.review.QuestReviewRoute
import com.byeboo.app.presentation.quest.review.common.other.CommonOtherAnswerRoute
import com.byeboo.app.presentation.quest.review.common.personal.MyAnswerRoute
import com.byeboo.app.presentation.quest.review.common.personal.detail.MyAnswerDetailRoute
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

fun NavController.navigateToQuestAiAnswer(
    questId: Long,
    isExistedAiAnswer: Boolean,
    aiAnswerOrigin: AiAnswerOrigin,
    navOptions: NavOptions? = null,
) {
    navigate(
        QuestAiAnswer(questId = questId, isExistedAiAnswer = isExistedAiAnswer, aiAnswerOrigin = aiAnswerOrigin),
        navOptions,
    )
}

fun NavGraphBuilder.questGraph(
    navigateUp: () -> Unit,
    navigateToQuest: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToQuestRecording: (Long) -> Unit,
    navigateToQuestBehavior: (Long) -> Unit,
    navigateToQuestCommonWriting: (Long, String) -> Unit,
    navigateToQuestReview: (Long) -> Unit,
    navigateToOffboardingCompletedGuide: () -> Unit,
    navigateToQuestRecordingComplete: (Long) -> Unit,
    navigateToQuestRecordingEdit: (Long, Boolean) -> Unit,
    navigateToQuestBehaviorEdit: (Long, Boolean, String) -> Unit,
    navigateToQuestCommonEdit: (Long, String, Boolean) -> Unit,
    navigateToQuestTip: (Long, QuestType) -> Unit,
    navigateToQuestBehaviorComplete: (Long) -> Unit,
    navigateToQuestCommonAnswer: (Long) -> Unit,
    navigateToQuestMyAnswers: () -> Unit,
    navigateToQuestMyAnswerDetail: (Long) -> Unit,
    navigateToQuestAiAnswer: (Long, Boolean, AiAnswerOrigin) -> Unit,
    navigateToQuestFromComplete: () -> Unit,
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

        composable<Quest> { backStackEntry ->
            val isCommonAnswerCompleted by backStackEntry.savedStateHandle
                .getStateFlow(QuestResultKey.COMMON_COMPLETED, false)
                .collectAsStateWithLifecycle()

            QuestRoute(
                navigateToQuestTip = navigateToQuestTip,
                navigateToQuestRecording = navigateToQuestRecording,
                navigateToQuestBehavior = navigateToQuestBehavior,
                navigateToQuestCommonWriting = navigateToQuestCommonWriting,
                navigateToQuestReview = navigateToQuestReview,
                navigateToCommonAnswer = navigateToQuestCommonAnswer,
                navigateToQuestMyAnswers = navigateToQuestMyAnswers,
                isCommonAnswerCompleted = isCommonAnswerCompleted,
                onCommonAnswerCompleted = {
                    backStackEntry.savedStateHandle[QuestResultKey.COMMON_COMPLETED] = false
                },
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
                navigateToQuestAiAnswer = navigateToQuestAiAnswer,
                paddingValues = paddingValues,
            )
        }

        composable<QuestCommonAnswer> {
            CommonOtherAnswerRoute(
                navigateToQuest = navigateUp,
                paddingValues = paddingValues,
            )
        }

        composable<QuestMyAnswers> {
            MyAnswerRoute(
                navigateUp = navigateUp,
                navigateToQuest = navigateToQuest,
                navigateToQuestMyAnswerDetail = navigateToQuestMyAnswerDetail,
                paddingValues = paddingValues,
            )
        }

        composable<QuestMyAnswersDetail> {
            MyAnswerDetailRoute(
                navigateUp = navigateUp,
                navigateToQuestMyAnswers = navigateToQuestMyAnswers,
                navigateToQuestCommonEdit = navigateToQuestCommonEdit,
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
            navigateToQuestAiAnswer = navigateToQuestAiAnswer,
            paddingValues = paddingValues,
        )

        questBehaviorGraph(
            navigateToQuest = navigateToQuest,
            navigateToQuestTip = navigateToQuestTip,
            navigateToQuestBehaviorComplete = navigateToQuestBehaviorComplete,
            navigateToQuestReview = navigateToQuestReview,
            navigateToOffboardingCompletedGuide = navigateToOffboardingCompletedGuide,
            navigateUp = navigateUp,
            navigateToQuestAiAnswer = navigateToQuestAiAnswer,
            paddingValues = paddingValues,
        )

        questCommonGraph(
            navigateToQuestFromComplete = navigateToQuestFromComplete,
            navigateUp = navigateUp,
            paddingValues = paddingValues,
        )

        composable<QuestAiAnswer> {
            QuestAiAnswerRoute(
                navigateToQuest = navigateToQuest,
                navigateUp = navigateUp,
                paddingValues = paddingValues,
            )
        }
    }
}
