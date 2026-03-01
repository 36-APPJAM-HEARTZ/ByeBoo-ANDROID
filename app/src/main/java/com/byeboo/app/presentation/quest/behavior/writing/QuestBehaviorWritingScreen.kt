package com.byeboo.app.presentation.quest.behavior.writing

import QuestPhotoPicker
import android.content.Context
import android.net.Uri
import android.view.KeyEvent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.byeboo.app.core.designsystem.component.tag.MiddleTag
import com.byeboo.app.core.designsystem.event.LocalSnackBarTrigger
import com.byeboo.app.core.designsystem.type.EmotionChipType
import com.byeboo.app.core.designsystem.type.MiddleTagType
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.core.util.addFocusCleaner
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.quest.component.bottomsheet.ByeBooBottomSheet
import com.byeboo.app.presentation.quest.component.card.QuestCompleteDialog
import com.byeboo.app.presentation.quest.component.modal.QuestQuitModal
import com.byeboo.app.presentation.quest.component.text.QuestWritingFooter
import com.byeboo.app.presentation.quest.component.text.QuestWritingTitle
import com.byeboo.app.presentation.quest.component.text.textfield.QuestTextField
import com.byeboo.app.presentation.quest.component.topbar.QuestWritingTopBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestBehaviorWritingRoute(
    navigateToQuest: () -> Unit,
    navigateToQuestTip: (Long, QuestType) -> Unit,
    navigateToQuestBehaviorComplete: (Long) -> Unit,
    navigateToQuestReview: (Long) -> Unit,
    navigateUp: () -> Unit,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: QuestBehaviorViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val showSnackBar = LocalSnackBarTrigger.current

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is QuestBehaviorSideEffect.NavigateToQuest -> navigateToQuest()
                is QuestBehaviorSideEffect.NavigateToQuestTip ->
                    navigateToQuestTip(
                        effect.questId,
                        effect.questType,
                    )

                is QuestBehaviorSideEffect.NavigateToQuestBehaviorComplete ->
                    navigateToQuestBehaviorComplete(
                        effect.questId,
                    )

                is QuestBehaviorSideEffect.CompleteAndClear -> viewModel.clearQuestInput()
                is QuestBehaviorSideEffect.NavigateToQuestReview ->
                    navigateToQuestReview(
                        effect.questId,
                    )

                is QuestBehaviorSideEffect.NavigateUp -> navigateUp()
                is QuestBehaviorSideEffect.ShowSnackBar -> showSnackBar(effect.message, effect.iconType)
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
        )
    }

    if (uiState.showCompleteModal) {
        QuestCompleteDialog(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
        )

        LaunchedEffect(Unit) {
            delay(2000L)
            viewModel.onCompleteModalTimeout()
        }
    }

    BackHandler { viewModel.onBackClicked() }

    QuestBehaviorWritingScreen(
        uiState = uiState,
        paddingValues = paddingValues,
        onBackClick = viewModel::onBackClicked,
        onTipClick = viewModel::onTipClicked,
        onUpdateSelectedImage = viewModel::updateSelectedImage,
        onUpdateContent = viewModel::updateContent,
        onSaveClick = viewModel::onSaveClicked,
        onCompleteClick = viewModel::onCompleteClicked,
        onBottomSheetDismiss = viewModel::closeBottomSheet,
        onEmotionSelected = { selectedEmotion -> viewModel.updateSelectedEmotion(selectedEmotion) },
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuestBehaviorWritingScreen(
    uiState: QuestBehaviorState,
    paddingValues: PaddingValues,
    onBackClick: () -> Unit,
    onTipClick: () -> Unit,
    onUpdateSelectedImage: (Uri?) -> Unit,
    onCompleteClick: (Context) -> Unit,
    onUpdateContent: (String) -> Unit,
    onSaveClick: (Context) -> Unit,
    onBottomSheetDismiss: () -> Unit,
    onEmotionSelected: (EmotionChipType?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val isFocused = remember { mutableStateOf(false) }
    val displayImageUri: Uri? =
        uiState.selectedImageUri
            ?: uiState.imageUrl.takeIf { it.isNotBlank() }?.toUri()

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
            onCompleteClick = {
                onCompleteClick(context)
                onUpdateSelectedImage(uiState.selectedImageUri)
            },
        )

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = screenWidthDp(24.dp)),
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

            QuestPhotoSection(
                imageCount = uiState.imageCount,
                displayImageUri = displayImageUri,
                onUpdateSelectedImage = onUpdateSelectedImage,
            )

            Spacer(modifier = modifier.height(screenHeightDp(20.dp)))

            QuestWritingSection(
                questAnswer = uiState.questAnswer,
                onFocusChanged = { isFocused.value = it },
                onUpdateContent = onUpdateContent,
                scrollState = scrollState,
            )

            Spacer(modifier = Modifier.height(screenHeightDp(32.dp)))

            QuestWritingFooter(
                currentCharCount = uiState.questAnswer.length,
                isPhotoQuestion = true,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = screenHeightDp(14.dp)),
            )
        }
    }

    ByeBooBottomSheet(
        selectedEmotion = uiState.selectedEmotion,
        navigateButton = { onSaveClick(context) },
        showBottomSheet = uiState.showBottomSheet,
        onDismiss = onBottomSheetDismiss,
        onEmotionSelected = onEmotionSelected,
        isUploading = uiState.isUploading,
    )
}

@Composable
private fun QuestPhotoSection(
    imageCount: Int,
    displayImageUri: Uri?,
    onUpdateSelectedImage: (Uri?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(screenHeightDp(12.dp)),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(screenWidthDp(8.dp)),
        ) {
            MiddleTag(
                middleTagType = MiddleTagType.QUEST_ESSENTIAL,
                text = "필수",
                textStyle = ByeBooTheme.typography.cap1,
            )

            Text(
                text = "사진 첨부",
                color = ByeBooTheme.colors.gray50,
                style = ByeBooTheme.typography.body2,
            )

            Text(
                text = "($imageCount/1)",
                color = ByeBooTheme.colors.gray400,
                style = ByeBooTheme.typography.body6,
            )
        }

        QuestPhotoPicker(
            imageUrl = displayImageUri,
            onImageClick = { url ->
                onUpdateSelectedImage(url)
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun QuestWritingSection(
    questAnswer: String,
    onUpdateContent: (String) -> Unit,
    onFocusChanged: (Boolean) -> Unit,
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(screenHeightDp(8.dp)),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(screenWidthDp(8.dp)),
        ) {
            MiddleTag(
                middleTagType = MiddleTagType.QUEST_OPTIONAL,
                text = "선택",
                textStyle = ByeBooTheme.typography.cap1,
            )

            Text(
                text = "생각 적기",
                color = ByeBooTheme.colors.gray50,
                style = ByeBooTheme.typography.body2,
            )
        }

        QuestTextField(
            value = questAnswer,
            onValueChange = {
                if (it.length <= 200) {
                    onUpdateContent(it)
                }
            },
            placeholder = "꼭 적지 않아도 괜찮지만, 글로 정리해 보면 스스로에게 한 걸음 더 가까워질 수 있어요.",
            onFocusChanged = onFocusChanged,
            scrollState = scrollState,
        )
    }
}
