package com.byeboo.app.presentation.quest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.byeboo.app.core.designsystem.component.LoadingScreen
import com.byeboo.app.core.designsystem.event.LocalSnackBarTrigger
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.domain.model.home.HomeStatus
import com.byeboo.app.presentation.quest.component.card.QuestCompleteDialog
import com.byeboo.app.presentation.quest.component.modal.QuestModal
import com.byeboo.app.presentation.quest.component.tab.QuestTabRow
import com.byeboo.app.presentation.quest.model.QuestSideEffect
import com.byeboo.app.presentation.quest.model.QuestTab
import com.byeboo.app.presentation.quest.screen.CommonJourneyScreen
import com.byeboo.app.presentation.quest.screen.JourneyCompleteScreen
import com.byeboo.app.presentation.quest.screen.MyJourneyScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate

@Composable
fun QuestRoute(
    navigateToQuestTip: (Long, QuestType) -> Unit,
    navigateToQuestRecording: (Long) -> Unit,
    navigateToQuestBehavior: (Long) -> Unit,
    navigateToQuestCommonWriting: (Long, String) -> Unit,
    navigateToQuestReview: (Long) -> Unit,
    navigateToCommonAnswer: (Long) -> Unit,
    navigateToMyAnswerDetail: (Long) -> Unit,
    navigateToQuestMyAnswers: () -> Unit,
    navigateToOffboardingCompletedGuide: () -> Unit,
    paddingValues: PaddingValues,
    isCommonAnswerCompleted: Boolean,
    onCommonAnswerCompleted: () -> Unit,
    viewModel: QuestViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val showSnackBar = LocalSnackBarTrigger.current

    LaunchedEffect(uiState.myJourneyState.currentStepIndex) {
        val questGroups = uiState.myJourneyState.questGroups
        val currentStepIndex = uiState.myJourneyState.currentStepIndex

        if (questGroups.isNotEmpty() && currentStepIndex >= 0) {
            val scrollIndex =
                questGroups
                    .take(currentStepIndex)
                    .sumOf { 1 + (it.quests.size + 2) / 3 }
            listState.animateScrollToItem(index = scrollIndex)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is QuestSideEffect.NavigateToQuestTip ->
                    navigateToQuestTip(effect.questId, effect.questType)
                is QuestSideEffect.NavigateToQuestRecording ->
                    navigateToQuestRecording(effect.questId)
                is QuestSideEffect.NavigateToQuestBehavior ->
                    navigateToQuestBehavior(effect.questId)
                is QuestSideEffect.NavigateToQuestCommonWriting ->
                    navigateToQuestCommonWriting(effect.questId, effect.question)
                is QuestSideEffect.NavigateToCommonAnswerDetail ->
                    navigateToCommonAnswer(effect.answerId)
                is QuestSideEffect.NavigateToQuestMyAnswersDetail ->
                    navigateToMyAnswerDetail(effect.answerId)
                is QuestSideEffect.NavigateToQuestReview ->
                    navigateToQuestReview(effect.questId)
                is QuestSideEffect.NavigateToQuestMyAnswers ->
                    navigateToQuestMyAnswers()
                is QuestSideEffect.NavigateToOffboardingCompletedGuide ->
                    navigateToOffboardingCompletedGuide()
                is QuestSideEffect.ShowSnackBar ->
                    showSnackBar(effect.snackBarType)
            }
        }
    }

    LaunchedEffect(isCommonAnswerCompleted) {
        if (isCommonAnswerCompleted) {
            viewModel.onCommonQuestCompleted()
            onCommonAnswerCompleted()
        }
    }

    if (uiState.showCompleteModal) {
        QuestCompleteDialog(
            modifier = Modifier.padding(horizontal = screenWidthDp(24.dp)),
        )

        LaunchedEffect(Unit) {
            delay(2000L)
            viewModel.closeCompleteModal()
        }
    }

    if (uiState.isStatusLoading) {
        LoadingScreen()
        return
    }

    QuestScreen(
        uiState = uiState,
        listState = listState,
        paddingValues = paddingValues,
        onQuestClick = viewModel::onQuestClicked,
        onMyAnswersClick = viewModel::onMyAnswersClicked,
        onCommonQuestClick = viewModel::onCommonQuestClicked,
        onDismissModal = viewModel::onQuitDismissModal,
        onTipClick = viewModel::onTipClicked,
        onQuestStart = viewModel::onQuestStart,
        onTabClick = viewModel::onTabClicked,
        onMeetingBoriClick = viewModel::onMeetingBoriClicked,
        onCommonAnswerClick = viewModel::onOtherAnswerClicked,
        onDateChange = viewModel::onDateChange,
        onLoadMore = viewModel::loadNextPage,
        onHeartClick = viewModel::onHeartClicked,
    )
}

@Composable
private fun QuestScreen(
    uiState: QuestUiState,
    listState: LazyListState,
    paddingValues: PaddingValues,
    onQuestClick: (Long) -> Unit,
    onMyAnswersClick: () -> Unit,
    onCommonQuestClick: (Long) -> Unit,
    onDismissModal: () -> Unit,
    onTipClick: () -> Unit,
    onQuestStart: () -> Unit,
    onTabClick: (QuestTab) -> Unit,
    onMeetingBoriClick: () -> Unit,
    onCommonAnswerClick: (Long) -> Unit,
    onDateChange: (LocalDate) -> Unit,
    onLoadMore: () -> Unit,
    onHeartClick: () -> Unit,
) {
    if (uiState.myJourneyState.showQuitModal) {
        QuestModal(
            onDismissRequest = onDismissModal,
            questNumber = uiState.myJourneyState.selectedQuest?.questNumber ?: 0L,
            questQuestion =
                uiState.myJourneyState.selectedQuest
                    ?.questQuestion
                    .orEmpty(),
            navigateToTip = onTipClick,
            progressButton = onQuestStart,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = screenWidthDp(48.dp)),
        )
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(ByeBooTheme.colors.background)
                .padding(
                    top = paddingValues.calculateTopPadding() + screenHeightDp(43.dp),
                    bottom = paddingValues.calculateBottomPadding(),
                ),
    ) {
        QuestTabRow(
            selectedTab = uiState.selectedTab,
            onTabSelected = onTabClick,
        )

        Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

        when (uiState.selectedTab) {
            QuestTab.MY_JOURNEY -> {
                when (uiState.status) {
                    HomeStatus.JOURNEY_COMPLETE -> {
                        JourneyCompleteScreen(
                            username = uiState.userName,
                            onClick = onMeetingBoriClick,
                        )
                    }
                    else -> {
                        MyJourneyScreen(
                            state = uiState.myJourneyState,
                            userName = uiState.userName,
                            listState = listState,
                            onQuestClick = onQuestClick,
                        )
                    }
                }
            }
            QuestTab.COMMON_JOURNEY -> {
                CommonJourneyScreen(
                    state = uiState.commonJourneyState,
                    onMyAnswersClick = onMyAnswersClick,
                    onAnswerClick = onCommonAnswerClick,
                    onDateChange = onDateChange,
                    onCommonQuestClick = onCommonQuestClick,
                    onLoadMore = onLoadMore,
                    onHeartClick = onHeartClick,
                )
            }
        }
    }
}
