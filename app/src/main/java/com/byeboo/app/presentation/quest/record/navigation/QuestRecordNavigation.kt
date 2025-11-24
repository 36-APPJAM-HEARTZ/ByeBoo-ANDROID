package com.byeboo.app.presentation.quest.record.navigation

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.core.util.routeNavigation
import com.byeboo.app.presentation.quest.record.QuestRecordingCompleteRoute
import com.byeboo.app.presentation.quest.record.QuestRecordingRoute
import com.byeboo.app.presentation.quest.record.navigation.QuestRecord.QuestRecording
import com.byeboo.app.presentation.quest.record.navigation.QuestRecord.QuestRecordingComplete

fun NavController.navigateToQuestRecording(questId: Long, isEditMode: Boolean = false, navOptions: NavOptions? = null) {
    navigate(QuestRecording(questId = questId, isEditMode = isEditMode), navOptions)
}

fun NavController.navigateToQuestRecordingComplete(questId: Long, navOptions: NavOptions? = null) {
    navigate(QuestRecordingComplete(questId), navOptions)
}

fun NavGraphBuilder.questRecordGraph(
    navigateToQuest: () -> Unit,
    navigateToQuestTip: (Long, QuestType) -> Unit,
    navigateToQuestRecordingComplete: (Long) -> Unit,
    navigateToQuestReview: (Long) -> Unit,
    navigateToOffboardingCompletedGuide: () -> Unit,
    navigateUp: () -> Unit,
    bottomPadding: Dp
) {
    routeNavigation<QuestRecord, QuestRecording> {
        composable<QuestRecording> {
            QuestRecordingRoute(
                navigateToQuest = navigateToQuest,
                navigateToQuestTip = navigateToQuestTip,
                navigateToQuestRecordingComplete = navigateToQuestRecordingComplete,
                navigateToQuestReview = navigateToQuestReview,
                navigateUp = navigateUp,
                bottomPadding = bottomPadding
            )
        }

        composable<QuestRecordingComplete> {
            QuestRecordingCompleteRoute(
                navigateToQuest = navigateToQuest,
                navigateToOffboardingCompletedGuide = navigateToOffboardingCompletedGuide,
                bottomPadding = bottomPadding
            )
        }
    }
}
