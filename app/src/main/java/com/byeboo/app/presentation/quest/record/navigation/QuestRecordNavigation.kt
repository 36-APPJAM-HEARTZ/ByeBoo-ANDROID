package com.byeboo.app.presentation.quest.record.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.core.util.routeNavigation
import com.byeboo.app.presentation.quest.navigation.AiAnswerEntryPoint
import com.byeboo.app.presentation.quest.record.complete.QuestRecordingCompleteRoute
import com.byeboo.app.presentation.quest.record.navigation.QuestRecord.QuestRecordingComplete
import com.byeboo.app.presentation.quest.record.navigation.QuestRecord.QuestRecordingWriting
import com.byeboo.app.presentation.quest.record.writing.QuestRecordingRoute

fun NavController.navigateToQuestRecording(
    questId: Long,
    isEditMode: Boolean = false,
    fromOffboarding: Boolean = false,
    navOptions: NavOptions? = null,
) {
    navigate(
        QuestRecordingWriting(
            questId = questId,
            isEditMode = isEditMode,
            fromOffboarding = fromOffboarding,
        ),
        navOptions,
    )
}

fun NavController.navigateToQuestRecordingComplete(
    questId: Long,
    navOptions: NavOptions? = null,
) {
    navigate(QuestRecordingComplete(questId), navOptions)
}

fun NavGraphBuilder.questRecordGraph(
    navigateToQuest: () -> Unit,
    navigateToQuestTip: (Long, QuestType) -> Unit,
    navigateToQuestRecordingComplete: (Long) -> Unit,
    navigateToQuestReview: (Long) -> Unit,
    navigateToOffboardingCompletedGuide: () -> Unit,
    navigateToQuestAiAnswer: (Long, Boolean, AiAnswerEntryPoint) -> Unit,
    navigateUp: () -> Unit,
    paddingValues: PaddingValues,
) {
    routeNavigation<QuestRecord, QuestRecordingWriting> {
        composable<QuestRecordingWriting> {
            QuestRecordingRoute(
                navigateToQuest = navigateToQuest,
                navigateToQuestTip = navigateToQuestTip,
                navigateToQuestRecordingComplete = navigateToQuestRecordingComplete,
                navigateToQuestReview = navigateToQuestReview,
                navigateUp = navigateUp,
                paddingValues = paddingValues,
            )
        }

        composable<QuestRecordingComplete> {
            QuestRecordingCompleteRoute(
                navigateToQuest = navigateToQuest,
                navigateToOffboardingCompletedGuide = navigateToOffboardingCompletedGuide,
                navigateToQuestAiAnswer = navigateToQuestAiAnswer,
                paddingValues = paddingValues,
            )
        }
    }
}
