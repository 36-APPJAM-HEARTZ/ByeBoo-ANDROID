package com.byeboo.app.presentation.home

import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.domain.model.home.HomeStatus

data class HomeUiState(
    val status: HomeStatus = HomeStatus.INITIAL_START,
    val journey: String = "",
    val currentStep: Long = 0L,
    val totalSteps: Int = 30,
    val nickname: String = "하츠핑",
    val hasSeenAboutHelp: Boolean = false,
    val isLoading: Boolean = true,
    val hasError: Boolean = false,
    val isBubbleClicked: Boolean = false,
    val isBubbleEnabled: Boolean = true,
)

sealed interface HomeSideEffect {
    data object NavigateToQuest : HomeSideEffect
    data class NavigateToQuestStart(val journey: QuestType?) : HomeSideEffect
    data object NavigateToTutorial : HomeSideEffect
    data object NavigateToOffboardingCompletedGuide : HomeSideEffect
    data object NavigateToOffboardingNewJourney : HomeSideEffect
    data class ShowSnackBar(val message: String) : HomeSideEffect
}
