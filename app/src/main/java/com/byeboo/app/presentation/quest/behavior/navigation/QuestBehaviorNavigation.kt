package com.byeboo.app.presentation.quest.behavior.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.core.util.routeNavigation
import com.byeboo.app.presentation.quest.behavior.complete.QuestBehaviorCompleteRoute
import com.byeboo.app.presentation.quest.behavior.navigation.QuestBehavior.QuestBehaviorComplete
import com.byeboo.app.presentation.quest.behavior.navigation.QuestBehavior.QuestBehaviorWriting
import com.byeboo.app.presentation.quest.behavior.writing.QuestBehaviorWritingRoute
import com.byeboo.app.presentation.quest.navigation.AiAnswerOrigin

fun NavController.navigateToQuestBehavior(
    questId: Long,
    isEditMode: Boolean = false,
    fromOffboarding: Boolean = false,
    imageKey: String? = null,
    navOptions: NavOptions? = null,
) {
    navigate(
        QuestBehaviorWriting(
            questId = questId,
            isEditMode = isEditMode,
            fromOffboarding = fromOffboarding,
            imageKey = imageKey,
        ),
        navOptions,
    )
}

fun NavController.navigateToQuestBehaviorComplete(
    questId: Long,
    navOptions: NavOptions? = null,
) {
    navigate(QuestBehaviorComplete(questId), navOptions)
}

fun NavGraphBuilder.questBehaviorGraph(
    navigateToQuest: () -> Unit,
    navigateToQuestTip: (Long, QuestType) -> Unit,
    navigateToQuestBehaviorComplete: (Long) -> Unit,
    navigateToQuestReview: (Long) -> Unit,
    navigateUp: () -> Unit,
    navigateToQuestAiAnswer: (Long, Boolean, AiAnswerOrigin) -> Unit,
    paddingValues: PaddingValues,
) {
    routeNavigation<QuestBehavior, QuestBehaviorWriting> {
        composable<QuestBehaviorWriting> {
            QuestBehaviorWritingRoute(
                navigateToQuest = navigateToQuest,
                navigateToQuestTip = navigateToQuestTip,
                navigateToQuestBehaviorComplete = navigateToQuestBehaviorComplete,
                navigateToQuestReview = navigateToQuestReview,
                navigateUp = navigateUp,
                paddingValues = paddingValues,
            )
        }

        composable<QuestBehaviorComplete> {
            QuestBehaviorCompleteRoute(
                navigateToQuest = navigateToQuest,
                navigateToQuestAiAnswer = navigateToQuestAiAnswer,
                paddingValues = paddingValues,
            )
        }
    }
}
