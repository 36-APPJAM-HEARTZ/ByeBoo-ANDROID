package com.byeboo.app.presentation.quest.behavior

import QuestPhotoPicker
import android.content.Context
import android.net.Uri
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
import androidx.compose.ui.platform.LocalContext
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
import com.byeboo.app.domain.model.quest.QuestValidator
import com.byeboo.app.presentation.quest.component.bottomsheet.ByeBooBottomSheet
import com.byeboo.app.presentation.quest.component.modal.QuestQuitModal
import com.byeboo.app.presentation.quest.component.text.textfield.QuestTextField
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestBehaviorWritingRoute(
    questId: Long,
    navigateToQuest: () -> Unit,
    navigateToQuestTip: (Long, QuestType) -> Unit,
    navigateToQuestBehaviorComplete: (Long) -> Unit,
    bottomPadding: Dp,
    modifier: Modifier = Modifier,
    viewModel: QuestBehaviorViewModel = hiltViewModel()
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
                is QuestBehaviorSideEffect.NavigateToQuest -> navigateToQuest()
                is QuestBehaviorSideEffect.NavigateToQuestTip -> navigateToQuestTip(
                    effect.questId,
                    effect.questType
                )

                is QuestBehaviorSideEffect.NavigateToQuestBehaviorComplete -> navigateToQuestBehaviorComplete(
                    effect.questId
                )

                is QuestBehaviorSideEffect.CompleteAndClear -> viewModel.clearQuestInput()
                is QuestBehaviorSideEffect.ShowSnackBar -> showSnackBar(effect.message)
                else -> Unit
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

    QuestBehaviorWritingScreen(
        uiState = uiState,
        bottomPadding = bottomPadding,
        onBackClick = viewModel::onBackClicked,
        onTipClick = viewModel::onTipClicked,
        onUpdateSelectedImage = viewModel::updateSelectedImage,
        onUpdateContent = viewModel::updateContent,
        navigateButton = viewModel::uploadImage,
        onClickCompleteButton = viewModel::openBottomSheet,
        onBottomSheetDismiss = viewModel::closeBottomSheet,
        onEmotionSelected = { selectedEmotion -> viewModel.updateSelectedEmotion(selectedEmotion) },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuestBehaviorWritingScreen(
    uiState: QuestBehaviorState,
    bottomPadding: Dp,
    onBackClick: () -> Unit,
    onTipClick: () -> Unit,
    onUpdateSelectedImage: (Uri?) -> Unit,
    onClickCompleteButton: () -> Unit,
    onUpdateContent: (String) -> Unit,
    navigateButton: (Context) -> Unit,
    onBottomSheetDismiss: () -> Unit,
    onEmotionSelected: (LargeTagType?) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

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
            modifier = modifier
                .padding(
                    top = screenHeightDp((27.dp) + bottomPadding),
                    bottom = screenHeightDp(16.dp)
                )
                .align(Alignment.Start)
                .clickable { onBackClick() }
        )

        LazyColumn(
            modifier = modifier.fillMaxWidth()
        ) {
            item {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SmallTag(
                        tagText = "STEP ${uiState.stepNumber}",
                        tagColor = ByeBooTheme.colors.gray500
                    )

                    Spacer(modifier = modifier.width(screenWidthDp(12.dp)))

                    Text(
                        text = "${uiState.stepMissionTitle}",
                        color = ByeBooTheme.colors.gray500,
                        style = ByeBooTheme.typography.body2
                    )
                }

                Spacer(modifier = modifier.height(screenHeightDp(12.dp)))
            }

            item {
                Text(
                    text = "${uiState.questNumber}번째 퀘스트",
                    color = ByeBooTheme.colors.gray500,
                    textAlign = TextAlign.Center,
                    style = ByeBooTheme.typography.body5,
                    modifier = modifier.fillMaxWidth()
                )

                Spacer(modifier = modifier.height(screenHeightDp(12.dp)))
            }

            item {
                Text(
                    text = uiState.question,
                    color = ByeBooTheme.colors.gray100,
                    textAlign = TextAlign.Center,
                    style = ByeBooTheme.typography.head1,
                    modifier = modifier.fillMaxWidth()
                )

                Spacer(modifier = modifier.height(screenHeightDp(25.dp)))
            }

            item {
                Box(
                    modifier = modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    MiddleTag(
                        middleTagType = MiddleTagType.QUEST_TIP,
                        text = "작성 TIP",
                        textStyle = ByeBooTheme.typography.cap1,
                        modifier = modifier.clickable { onTipClick() }
                    )
                }

                Spacer(modifier = modifier.height(screenHeightDp(16.dp)))
            }

            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    MiddleTag(
                        middleTagType = MiddleTagType.QUEST_ESSENTIAL,
                        text = "필수",
                        textStyle = ByeBooTheme.typography.cap1
                    )

                    Spacer(modifier = modifier.width(screenWidthDp(8.dp)))

                    Text(
                        text = "사진 첨부",
                        color = ByeBooTheme.colors.gray50,
                        style = ByeBooTheme.typography.body2
                    )

                    Spacer(modifier = modifier.width(screenWidthDp(8.dp)))

                    Text(
                        text = "(${uiState.imageCount}/1)",
                        color = ByeBooTheme.colors.gray400,
                        style = ByeBooTheme.typography.body5
                    )
                }

                Spacer(modifier = modifier.height(screenHeightDp(8.dp)))
            }

            item {
                QuestPhotoPicker(
                    imageUrl = uiState.selectedImageUri,
                    onImageClick = { url ->
                        onUpdateSelectedImage(url)
                    }
                )

                Spacer(modifier = modifier.height(screenHeightDp(16.dp)))
            }

            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    MiddleTag(
                        middleTagType = MiddleTagType.QUEST_OPTIONAL,
                        text = "선택",
                        textStyle = ByeBooTheme.typography.cap1
                    )

                    Spacer(modifier = modifier.width(screenWidthDp(8.dp)))

                    Text(
                        text = "생각 적기",
                        color = ByeBooTheme.colors.gray50,
                        style = ByeBooTheme.typography.body2
                    )
                }

                Spacer(modifier = modifier.height(screenHeightDp(8.dp)))
            }

            item {
                Column {
                    QuestTextField(
                        questWritingState = uiState.contentState,
                        value = uiState.contents,
                        onValueChange = {
                            if (it.length <= 200) {
                                onUpdateContent(it)
                            }
                        },
                        placeholder = "꼭 적지 않아도 괜찮지만, 글로 정리해 보면 스스로에게 한 걸음 더 가까워질 수 있어요.",
                        isQuestion = false,
                        onFocusChanged = {
                            isFocused.value = it
                        },
                        modifier = modifier
                            .fillMaxWidth()
                            .bringIntoViewRequester(bringIntoViewRequester)
                    )
                }
            }

            item {
                Spacer(modifier = modifier.height(screenHeightDp(24.dp)))

                ByeBooActivationButton(
                    buttonDisableColor = ByeBooTheme.colors.whiteAlpha10,
                    buttonText = "완료",
                    buttonDisableTextColor = ByeBooTheme.colors.gray300,
                    onClick = {
                        onClickCompleteButton()
                        onUpdateSelectedImage(uiState.selectedImageUri)
                    },
                    isEnabled = QuestValidator.validButton(uiState.imageCount)
                )
            }
        }
    }

    ByeBooBottomSheet(
        selectedEmotion = uiState.selectedEmotion,
        navigateButton = { navigateButton(context) },
        showBottomSheet = uiState.showBottomSheet,
        onDismiss = onBottomSheetDismiss,
        onEmotionSelected = onEmotionSelected,
        isUploading = uiState.isUploading
    )
}
