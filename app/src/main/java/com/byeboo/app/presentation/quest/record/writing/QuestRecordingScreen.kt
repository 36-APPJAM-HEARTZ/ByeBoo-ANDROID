package com.byeboo.app.presentation.quest.record.writing

import android.view.KeyEvent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.nativeKeyCode
import androidx.compose.ui.input.key.onPreInterceptKeyBeforeSoftKeyboard
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.byeboo.app.core.designsystem.event.LocalSnackBarTrigger
import com.byeboo.app.core.designsystem.type.EmotionChipType
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.core.util.addFocusCleaner
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.quest.component.QuestWritingTopBar
import com.byeboo.app.presentation.quest.component.bottomsheet.ByeBooBottomSheet
import com.byeboo.app.presentation.quest.component.card.QuestCompleteCard
import com.byeboo.app.presentation.quest.component.modal.QuestQuitModal
import com.byeboo.app.presentation.quest.component.text.QuestWritingFooter
import com.byeboo.app.presentation.quest.component.text.QuestWritingTitle
import com.byeboo.app.presentation.quest.component.text.textfield.QuestTextField
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun QuestRecordingRoute(
    navigateToQuest: () -> Unit,
    navigateToQuestTip: (Long, QuestType) -> Unit,
    navigateToQuestRecordingComplete: (Long) -> Unit,
    navigateToQuestReview: (Long) -> Unit,
    navigateUp: () -> Unit,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: QuestRecordingViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val showSnackBar = LocalSnackBarTrigger.current

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is QuestRecordingSideEffect.NavigateToQuest -> navigateToQuest()
                is QuestRecordingSideEffect.NavigateToQuestTip ->
                    navigateToQuestTip(
                        effect.questId,
                        effect.questType,
                    )

                is QuestRecordingSideEffect.NavigateToQuestRecordingComplete ->
                    navigateToQuestRecordingComplete(
                        effect.questId,
                    )

                is QuestRecordingSideEffect.NavigateToQuestReview ->
                    navigateToQuestReview(
                        effect.questId,
                    )

                is QuestRecordingSideEffect.NavigateUp -> navigateUp()
                is QuestRecordingSideEffect.ShowSnackBar -> showSnackBar(effect.message)
            }
        }
    }

    if (uiState.showQuitModal) {
        QuestQuitModal(
            onDismissRequest = viewModel::onDismissModal,
            stayButton = viewModel::onDismissModal,
            quitButton = {
                viewModel.onDismissModal()
                viewModel.onQuitClicked()
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = screenWidthDp(48.dp)),
            dialogProperties = DialogProperties(usePlatformDefaultWidth = false),
        )
    }

    if (uiState.showCompleteModal) {
        Dialog(
            onDismissRequest = {},
            properties =
                DialogProperties(
                    usePlatformDefaultWidth = false,
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false,
                ),
        ) {
            QuestCompleteCard(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = screenWidthDp(24.dp)),
            )
        }
        LaunchedEffect(Unit) {
            delay(2000L)
            viewModel.onCompleteModalTimeout()
        }
    }

    BackHandler { viewModel.onBackClicked() }

    QuestRecordingScreen(
        uiState = uiState,
        paddingValues = paddingValues,
        onBackClick = viewModel::onBackClicked,
        onTipClick = viewModel::onTipClicked,
        onCompleteClick = viewModel::onCompleteClicked,
        onUpdateContent = viewModel::updateContent,
        onSaveClick = viewModel::onSaveClicked,
        onBottomSheetDismiss = viewModel::closeBottomSheet,
        onEmotionSelected = { selectedEmotion -> viewModel.updateSelectedEmotion(selectedEmotion) },
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuestRecordingScreen(
    uiState: QuestRecordingState,
    paddingValues: PaddingValues,
    onBackClick: () -> Unit,
    onTipClick: () -> Unit,
    onCompleteClick: () -> Unit,
    onUpdateContent: (Boolean, String) -> Unit,
    onSaveClick: () -> Unit,
    onBottomSheetDismiss: () -> Unit,
    onEmotionSelected: (EmotionChipType?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val isFocused = remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val density = LocalDensity.current
    val isImeVisible = WindowInsets.ime.getBottom(density) > 0

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(ByeBooTheme.colors.background)
                .onPreInterceptKeyBeforeSoftKeyboard { event ->
                    if (event.key.nativeKeyCode == KeyEvent.KEYCODE_BACK) {
                        focusManager.clearFocus(force = true)
                        isFocused.value = false
                        true
                    } else {
                        false
                    }
                }.addFocusCleaner(focusManager)
                .imePadding()
                .padding(
                    top = paddingValues.calculateTopPadding() + screenHeightDp(43.dp),
                    bottom = if (isImeVisible) 0.dp else paddingValues.calculateBottomPadding(),
                ),
    ) {
        QuestWritingTopBar(
            isEnabled = uiState.isCompleteButtonEnabled,
            onBackClick = onBackClick,
            onCompleteClick = onCompleteClick,
        )

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1f, false)
                    .verticalScroll(scrollState)
                    .padding(horizontal = screenWidthDp(24.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

            QuestWritingTitle(
                questNumber = uiState.questNumber,
                question = uiState.question,
                onTipClick = onTipClick,
            )

            Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

            HorizontalDivider(
                thickness = 1.dp,
                color = ByeBooTheme.colors.gray800,
            )

            Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))

            QuestTextField(
                value = uiState.questAnswer,
                onValueChange = {
                    if (it.length <= 500) {
                        onUpdateContent(isFocused.value, it)
                    }
                },
                placeholder = "글로 적다 보면, 스스로에게 한 걸음 더 가까워질 수 있어요.",
                onFocusChanged = {
                    isFocused.value = it
                },
                scrollState = scrollState,
            )
            Spacer(modifier = Modifier.height(screenHeightDp(32.dp)))
        }

        QuestWritingFooter(
            currentCharCount = uiState.questAnswer.length,
            isPhotoQuestion = false,
            modifier = Modifier.padding(bottom = screenHeightDp(14.dp))
        )
    }

    ByeBooBottomSheet(
        selectedEmotion = uiState.selectedEmotion,
        navigateButton = onSaveClick,
        showBottomSheet = uiState.showBottomSheet,
        onDismiss = onBottomSheetDismiss,
        onEmotionSelected = onEmotionSelected,
    )
}
