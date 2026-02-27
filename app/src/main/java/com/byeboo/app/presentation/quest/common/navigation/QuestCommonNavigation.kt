package com.byeboo.app.presentation.quest.common.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.byeboo.app.core.util.routeNavigation
import com.byeboo.app.presentation.quest.common.navigation.QuestCommonRoute.QuestCommonWriting
import com.byeboo.app.presentation.quest.common.navigation.QuestCommonRoute.QuestCommonComplete
import com.byeboo.app.presentation.quest.common.writing.QuestCommonRoute

fun NavController.navigateToQuestCommon(
    questId: Long,
    navOptions: NavOptions? = null,
) {
    navigate(
        QuestCommonWriting(
            questId = questId,
        ),
        navOptions,
    )
}

fun NavController.navigateToQuestCommonComplete(
    questId: Long,
    navOptions: NavOptions? = null
) {
    navigate(QuestCommonComplete(questId), navOptions)
}

fun NavGraphBuilder.questCommonGraph(
    navigateToQuest: () -> Unit,
    navigateToQuestCommonComplete: (Long) -> Unit,
    navigateUp: () -> Unit,
    paddingValues: PaddingValues,
) {
    routeNavigation<QuestCommonRoute, QuestCommonWriting> {
        composable<QuestCommonWriting> {
            QuestCommonRoute(
                navigateToQuest = navigateToQuest,
                navigateToQuestCommonComplete = navigateToQuestCommonComplete,
                navigateUp = navigateUp,
                paddingValues = paddingValues
            )
        }
    }
}