package com.byeboo.app.presentation.quest.behavior.navigation

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.core.util.routeNavigation
import com.byeboo.app.presentation.quest.behavior.QuestBehaviorCompleteRoute
import com.byeboo.app.presentation.quest.behavior.QuestBehaviorWritingRoute
import com.byeboo.app.presentation.quest.behavior.navigation.QuestBehavior.QuestBehaviorComplete
import com.byeboo.app.presentation.quest.behavior.navigation.QuestBehavior.QuestBehaviorWriting

fun NavController.navigateToQuestBehavior(questId: Long, isEditMode: Boolean = false, imageKey: String? = null, navOptions: NavOptions? = null) {
    navigate(QuestBehaviorWriting(questId, isEditMode, imageKey), navOptions)
}

fun NavController.navigateToQuestBehaviorComplete(questId: Long, navOptions: NavOptions? = null) {
    navigate(QuestBehaviorComplete(questId), navOptions)
}

fun NavGraphBuilder.questBehaviorGraph(
    navigateToQuest: () -> Unit,
    navigateToQuestTip: (Long, QuestType) -> Unit,
    navigateToQuestBehaviorComplete: (Long) -> Unit,
    navigateToOffboardingCompletedGuide: () -> Unit,
    navigateToQuestReview: (Long) -> Unit,
    navigateUp: () -> Unit,
    bottomPadding: Dp
) {
    routeNavigation<QuestBehavior, QuestBehaviorWriting> {
        composable<QuestBehaviorWriting> {
            QuestBehaviorWritingRoute(
                navigateToQuest = navigateToQuest,
                navigateToQuestTip = navigateToQuestTip,
                navigateToQuestBehaviorComplete = navigateToQuestBehaviorComplete,
                navigateToQuestReview = navigateToQuestReview,
                navigateUp = navigateUp,
                bottomPadding = bottomPadding
            )
        }

        composable<QuestBehaviorComplete> {
            QuestBehaviorCompleteRoute(
                navigateToQuest = navigateToQuest,
                navigateToOffboardingCompletedGuide = navigateToOffboardingCompletedGuide,
                bottomPadding = bottomPadding
            )
        }
    }
}
