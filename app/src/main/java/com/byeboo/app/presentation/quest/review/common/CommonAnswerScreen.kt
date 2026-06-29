package com.byeboo.app.presentation.quest.review.common

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imeAnimationTarget
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.byeboo.app.core.designsystem.event.LocalSnackBarTrigger
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.quest.component.bottomsheet.MoreOptionsBottomSheet
import com.byeboo.app.presentation.quest.component.bottomsheet.ReplyBottomSheet
import com.byeboo.app.presentation.quest.component.card.CommonAnswerItem
import com.byeboo.app.presentation.quest.component.card.CommonReplyItem
import com.byeboo.app.presentation.quest.component.input.CommentInputBar
import com.byeboo.app.presentation.quest.component.modal.QuestDeleteModal
import com.byeboo.app.presentation.quest.component.text.QuestCommonTitle
import com.byeboo.app.presentation.quest.component.type.MoreOptionTarget
import com.byeboo.app.presentation.quest.component.type.MyPostOption
import com.byeboo.app.presentation.quest.component.type.OtherPostOption
import com.byeboo.app.presentation.quest.component.type.PostOption
import com.byeboo.app.presentation.quest.model.CommonReplyModel
import com.byeboo.app.presentation.quest.review.common.component.AnswerDetailTopBar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@Composable
fun CommonOtherAnswerRoute(
    navigateUp: () -> Unit,
    navigateToQuestMyAnswers: () -> Unit,
    navigateToQuestCommonEdit: (Long, String, Boolean) -> Unit,
    paddingValues: PaddingValues,
    viewModel: CommonOtherAnswerViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val showSnackBar = LocalSnackBarTrigger.current

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is CommonAnswerSideEffect.NavigateUp -> navigateUp()
                is CommonAnswerSideEffect.NavigateToQuestMyAnswers -> navigateToQuestMyAnswers()
                is CommonAnswerSideEffect.NavigateToQuestCommonEdit ->
                    navigateToQuestCommonEdit(
                        effect.answerId,
                        effect.question,
                        effect.isEditMode,
                    )
                is CommonAnswerSideEffect.ShowSnackBar -> showSnackBar(effect.snackBarType)
            }
        }
    }

    if (uiState.showDeleteModal) {
        QuestDeleteModal(
            onDismissRequest = viewModel::onDismissDeleteModal,
            onNoClick = viewModel::onDismissDeleteModal,
            onYesClick = viewModel::onQuestDeleteClicked,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = screenWidthDp(48.dp)),
        )
    }

    CommonOtherAnswerScreen(
        uiState = uiState,
        paddingValues = paddingValues,
        onBackClick = viewModel::onBackClicked,
        onMoreOptionsClick = { target -> viewModel.onClickMoreOptions(target) },
        onDismissBottomSheet = viewModel::onDismissBottomSheet,
        onOptionClick = { option -> viewModel.onOptionClicked(option) },
        onHeartClick = viewModel::onHeartClicked,
        onEditCommentComplete = viewModel::onEditCommentComplete,
        onCommentComplete = viewModel::onCommentComplete,
        onCommentClick = viewModel::onCommentClicked,
        onReplyComplete = viewModel::onReplyComplete,
        replySubmissionSuccess = viewModel.replySubmissionSuccess,
        onDismissReplyBottomSheet = viewModel::onDismissReplyBottomSheet,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun CommonOtherAnswerScreen(
    uiState: CommonAnswerState,
    paddingValues: PaddingValues,
    onBackClick: () -> Unit,
    onMoreOptionsClick: (MoreOptionTarget) -> Unit,
    onDismissBottomSheet: () -> Unit,
    onOptionClick: (PostOption) -> Unit,
    onHeartClick: () -> Unit,
    onEditCommentComplete: (String) -> Unit,
    onCommentComplete: (String) -> Unit,
    onCommentClick: (CommonReplyModel) -> Unit,
    onDismissReplyBottomSheet: () -> Unit,
    onReplyComplete: (String) -> Unit,
    replySubmissionSuccess: Flow<Unit>,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    var commentText by remember { mutableStateOf("") }
    val maxLength = 500
    val isCompleteEnabled = commentText.isNotEmpty()

    val density = LocalDensity.current
    val imeInsets = WindowInsets.ime
    val imeTarget = WindowInsets.imeAnimationTarget

    val bottomPadding =
        with(density) {
            maxOf(imeInsets.getBottom(density).toDp(), paddingValues.calculateBottomPadding())
        }

    var isKeyboardVisible by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var shouldScrollToBottom by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        snapshotFlow { imeTarget.getBottom(density) > 0 }
            .distinctUntilChanged()
            .collect { visible -> isKeyboardVisible = visible }
    }

    LaunchedEffect(isKeyboardVisible) {
        if (isKeyboardVisible) {
            focusRequester.requestFocus()
        }
    }

    LaunchedEffect(uiState.comments) {
        if (shouldScrollToBottom) {
            coroutineScope.launch {
                scrollState.animateScrollTo(scrollState.maxValue)
            }
            shouldScrollToBottom = false
        }
    }

    LaunchedEffect(uiState.editingComment) {
        val editing = uiState.editingComment

        if (editing?.target is MoreOptionTarget.Comment && !uiState.showReplyBottomSheet) {
            commentText = editing.content
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(ByeBooTheme.colors.background)
                .padding(
                    top = paddingValues.calculateTopPadding() + screenHeightDp(43.dp),
                    bottom = bottomPadding,
                ),
    ) {
        AnswerDetailTopBar(
            onBackClick = onBackClick,
            onMoreOptionsClick = {
                focusManager.clearFocus()
                keyboardController?.hide()
                uiState.answer?.let { answer ->
                    onMoreOptionsClick(
                        MoreOptionTarget.Answer(
                            id = answer.answerId,
                            writerId = answer.writerId,
                        ),
                    )
                }
            },
        )

        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = screenWidthDp(24.dp))
                    .padding(bottom = screenHeightDp(16.dp))
                    .pointerInput(Unit) {
                        detectTapGestures {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                    },
        ) {
            uiState.answer?.let { answer ->
                QuestCommonTitle(
                    createdAt = answer.displayTime,
                    questQuestion = uiState.questQuestion,
                )

                Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))

                CommonAnswerItem(
                    answer = answer,
                    onHeartClick = onHeartClick,
                    isExpanded = true,
                )
            }

            Spacer(modifier = Modifier.height(screenHeightDp(24.dp)))

            uiState.comments.forEach { reply ->
                CommonReplyItem(
                    reply = reply,
                    onMoreOptionsClick = {
                        onMoreOptionsClick(
                            MoreOptionTarget.Comment(
                                id = reply.replyId,
                                writerId = reply.writerId,
                            ),
                        )
                    },
                    onCommentClick = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        onCommentClick(reply)
                    },
                )
            }
        }

        CommentInputBar(
            commentText = commentText,
            isKeyboardVisible = isKeyboardVisible,
            focusRequester = focusRequester,
            isCompleteEnabled = isCompleteEnabled,
            maxLength = maxLength,
            onTextChange = { newText ->
                if (newText.length <= maxLength) commentText = newText
            },
            onCompleteClick = {
                if (uiState.editingComment?.target is MoreOptionTarget.Comment && !uiState.showReplyBottomSheet) {
                    onEditCommentComplete(commentText)
                } else {
                    onCommentComplete(commentText)
                    shouldScrollToBottom = true
                }
                commentText = ""
                keyboardController?.hide()
                focusManager.clearFocus()
                shouldScrollToBottom = true
            },
        )
    }

    if (uiState.selectedTarget != null) {
        if (uiState.isMine) {
            MoreOptionsBottomSheet(
                topOption = MyPostOption.EDIT,
                bottomOption = MyPostOption.DELETE,
                onOptionClick = onOptionClick,
                showBottomSheet = uiState.showBottomSheet,
                onDismissRequest = onDismissBottomSheet,
            )
        } else {
            MoreOptionsBottomSheet(
                topOption = OtherPostOption.BLOCK,
                bottomOption = OtherPostOption.REPORT,
                onOptionClick = onOptionClick,
                showBottomSheet = uiState.showBottomSheet,
                onDismissRequest = onDismissBottomSheet,
            )
        }
    }

    uiState.selectedComment?.let { comment ->
        ReplyBottomSheet(
            showBottomSheet = uiState.showReplyBottomSheet,
            comment = comment,
            replies = uiState.selectedReplies,
            onDismissRequest = onDismissReplyBottomSheet,
            onReplyComplete = onReplyComplete,
            isReplySubmitting = uiState.isReplySubmitting,
            replySubmissionSuccess = replySubmissionSuccess,
            onCommentMoreOptionsClick = {
                onMoreOptionsClick(
                    MoreOptionTarget.Comment(
                        id = comment.replyId,
                        writerId = comment.writerId,
                    ),
                )
            },
            onReplyMoreOptionsClick = { reply ->
                onMoreOptionsClick(
                    MoreOptionTarget.Reply(
                        id = reply.replyId,
                        writerId = reply.writerId,
                    ),
                )
            },
            editingComment = uiState.editingComment,
            onEditCommentComplete = onEditCommentComplete,
        )
    }
}
