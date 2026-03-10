package com.byeboo.app.presentation.quest.common.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.byeboo.app.core.util.routeNavigation
import com.byeboo.app.presentation.quest.common.navigation.QuestCommonRoute.QuestCommonWriting
import com.byeboo.app.presentation.quest.common.writing.QuestCommonRoute

fun NavController.navigateToQuestCommonWriting(
    questId: Long,
    question: String,
    answerId: Long? = null,
    isEditMode: Boolean = false,
    navOptions: NavOptions? = null,
) {
    navigate(
        QuestCommonWriting(
            questId = questId,
            question = question,
            answerId = answerId,
            isEditMode = isEditMode,
        ),
        navOptions,
    )
}

fun NavGraphBuilder.questCommonGraph(
    navigateToQuestFromComplete: () -> Unit,
    navigateUp: () -> Unit,
    paddingValues: PaddingValues,
) {
    routeNavigation<QuestCommonRoute, QuestCommonWriting> {
        composable<QuestCommonWriting> { backStackEntry ->
            val isEditMode = backStackEntry.toRoute<QuestCommonWriting>().isEditMode
            QuestCommonRoute(
                navigateToQuestFromComplete = if (isEditMode) navigateUp else navigateToQuestFromComplete,
                navigateUp = navigateUp,
                paddingValues = paddingValues,
            )
        }
    }
}
