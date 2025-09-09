package com.byeboo.app.presentation.quest.record

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.nativeKeyCode
import androidx.compose.ui.input.key.onPreInterceptKeyBeforeSoftKeyboard
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.component.button.ByeBooActivationButton
import com.byeboo.app.core.designsystem.component.tag.MiddleTag
import com.byeboo.app.core.designsystem.component.tag.SmallTag
import com.byeboo.app.core.designsystem.event.LocalSnackBarTrigger
import com.byeboo.app.core.designsystem.type.LargeTagType
import com.byeboo.app.core.designsystem.type.MiddleTagType
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.core.util.addFocusCleaner
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.domain.model.quest.QuestContentLengthValidator
import com.byeboo.app.presentation.quest.component.bottomsheet.ByeBooBottomSheet
import com.byeboo.app.presentation.quest.component.modal.QuestQuitModal
import com.byeboo.app.presentation.quest.component.text.textfield.QuestTextField
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun QuestRecordingRoute(
    questId: Long,
    navigateToQuest: () -> Unit,
    navigateToQuestTip: (Long, QuestType) -> Unit,
    navigateToQuestRecordingComplete: (Long) -> Unit,
    bottomPadding: Dp,
    modifier: Modifier = Modifier,
    viewModel: QuestRecordingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val showSnackBar = LocalSnackBarTrigger.current

    LaunchedEffect(questId) {
        viewModel.setQuestId(questId)
        viewModel.getQuestDetailInfo(questId)
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is QuestRecordingSideEffect.NavigateToQuest -> navigateToQuest()
                is QuestRecordingSideEffect.NavigateToQuestTip -> navigateToQuestTip(effect.questId, effect.questType)
                is QuestRecordingSideEffect.NavigateToQuestRecordingComplete -> navigateToQuestRecordingComplete(effect.questId)
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = screenWidthDp(48.dp)),
            dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
        )
    }

    BackHandler { viewModel.onBackClicked() }

    QuestRecordingScreen(
        uiState = uiState,
        bottomPadding = bottomPadding,
        onBackClick = viewModel::onBackClicked,
        onTipClick = viewModel::onTipClicked,
        onClickCompleteButton = viewModel::openBottomSheet,
        onUpdateContent = viewModel::updateContent,
        navigateButton = viewModel::postQuestRecording,
        onBottomSheetDismiss = viewModel::closeBottomSheet,
        onEmotionSelected = { selectedEmotion -> viewModel.updateSelectedEmotion(selectedEmotion) },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuestRecordingScreen(
    uiState: QuestRecordingState,
    bottomPadding: Dp,
    onBackClick: () -> Unit,
    onTipClick: () -> Unit,
    onClickCompleteButton: () -> Unit,
    onUpdateContent: (Boolean, String) -> Unit,
    navigateButton: () -> Unit,
    onBottomSheetDismiss: () -> Unit,
    onEmotionSelected: (LargeTagType?) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val isFocused = remember { mutableStateOf(false) }

    LaunchedEffect(isFocused.value) {
        if (isFocused.value) {
            delay(300)
            bringIntoViewRequester.bringIntoView()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = ByeBooTheme.colors.black)
            .onPreInterceptKeyBeforeSoftKeyboard { event ->
                if (event.key.nativeKeyCode == android.view.KeyEvent.KEYCODE_BACK) {
                    focusManager.clearFocus(force = true)
                    isFocused.value = false
                    true
                } else {
                    false
                }
            }
            .addFocusCleaner(focusManager)
            .padding(horizontal = screenWidthDp(24.dp))
            .padding(bottom = screenHeightDp(bottomPadding))
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_left),
            contentDescription = "back button",
            tint = ByeBooTheme.colors.white,
            modifier = Modifier
                .padding(
                    top = screenHeightDp((27.dp) + bottomPadding),
                    bottom = screenHeightDp(16.dp)
                )
                .align(Alignment.Start)
                .clickable { onBackClick() }
        )

        Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                Spacer(modifier = Modifier.height(screenHeightDp(10.dp)))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SmallTag(
                        tagText = "STEP ${uiState.stepNumber}",
                        tagColor = ByeBooTheme.colors.gray500
                    )

                    Spacer(modifier = Modifier.width(screenWidthDp(8.dp)))

                    Text(
                        text = uiState.step,
                        style = ByeBooTheme.typography.body2,
                        color = ByeBooTheme.colors.gray500
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))

                Text(
                    text = "${uiState.questNumber}번째 퀘스트",
                    modifier = Modifier.fillMaxWidth(),
                    color = ByeBooTheme.colors.gray500,
                    style = ByeBooTheme.typography.body5,
                    textAlign = TextAlign.Center
                )
            }

            item {
                Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))

                Text(
                    text = uiState.questQuestion,
                    modifier = Modifier.fillMaxWidth(),
                    color = ByeBooTheme.colors.gray100,
                    style = ByeBooTheme.typography.head1,
                    textAlign = TextAlign.Center
                )
            }

            item {
                Spacer(modifier = Modifier.height(screenHeightDp(25.dp)))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    MiddleTag(
                        middleTagType = MiddleTagType.QUEST_TIP,
                        text = "작성 TIP",
                        textStyle = ByeBooTheme.typography.cap1,
                        modifier = Modifier.clickable { onTipClick() }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(screenHeightDp(24.dp)))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .bringIntoViewRequester(bringIntoViewRequester)
                ) {
                    QuestTextField(
                        questWritingState = uiState.contentsState,
                        value = uiState.questAnswer,
                        onValueChange = {
                            if (it.length <= 500) {
                                onUpdateContent(isFocused.value, it)
                            }
                        },
                        placeholder = "글로 적다 보면, 스스로에게 한 걸음 더 가까워질 수 있어요.",
                        onFocusChanged = {
                            isFocused.value = it
                        }
                    )
                    Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

                    Text(
                        text = "*10글자 이상 작성해 주세요.",
                        style = ByeBooTheme.typography.cap2,
                        color = ByeBooTheme.colors.gray400,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(screenHeightDp(54.dp)))

                ByeBooActivationButton(
                    buttonDisableColor = ByeBooTheme.colors.whiteAlpha10,
                    buttonText = "완료하기",
                    buttonDisableTextColor = ByeBooTheme.colors.gray300,
                    onClick = onClickCompleteButton,
                    isEnabled = QuestContentLengthValidator.validButton(uiState.questAnswer)
                )

                Spacer(modifier = Modifier.height(screenHeightDp(8.dp)))
            }
        }
    }

    ByeBooBottomSheet(
        selectedEmotion = uiState.selectedEmotion,
        navigateButton = navigateButton,
        showBottomSheet = uiState.showBottomSheet,
        onDismiss = onBottomSheetDismiss,
        onEmotionSelected = onEmotionSelected
    )
}
