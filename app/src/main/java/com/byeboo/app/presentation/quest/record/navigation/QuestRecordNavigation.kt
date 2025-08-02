package com.byeboo.app.presentation.quest.record.navigation

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.core.util.routeNavigation
import com.byeboo.app.presentation.quest.record.QuestRecordingCompleteRoute
import com.byeboo.app.presentation.quest.record.QuestRecordingRoute
import com.byeboo.app.presentation.quest.record.navigation.QuestRecord.QuestRecording
import com.byeboo.app.presentation.quest.record.navigation.QuestRecord.QuestRecordingComplete

fun NavController.navigateToQuestRecording(questId: Long, navOptions: NavOptions? = null) {
    navigate(QuestRecording(questId), navOptions)
}

fun NavController.navigateToQuestRecordingComplete(questId: Long, navOptions: NavOptions? = null) {
    navigate(QuestRecordingComplete(questId), navOptions)
}

fun NavGraphBuilder.questRecordGraph(
    navigateToQuest: () -> Unit,
    navigateToQuestTip: (Long, QuestType) -> Unit,
    navigateToQuestRecordingComplete: (Long) -> Unit,
    bottomPadding: Dp
) {
    routeNavigation<QuestRecord, QuestRecording> {
        composable<QuestRecording> { backStackEntry ->
            val questRecording = backStackEntry.toRoute<QuestRecording>()
            val questId = questRecording.questId

            QuestRecordingRoute(
                questId = questId,
                navigateToQuest = navigateToQuest,
                navigateToQuestTip = navigateToQuestTip,
                navigateToQuestRecordingComplete = navigateToQuestRecordingComplete,
                bottomPadding = bottomPadding
            )
        }

        composable<QuestRecordingComplete> { backStackEntry ->
            val questRecordingComplete = backStackEntry.toRoute<QuestRecordingComplete>()
            val questId = questRecordingComplete.questId

            QuestRecordingCompleteRoute(
                questId = questId,
                navigateToQuest = navigateToQuest,
                bottomPadding = bottomPadding
            )
        }
    }
}
