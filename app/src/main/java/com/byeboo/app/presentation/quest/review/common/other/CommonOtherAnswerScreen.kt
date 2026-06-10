package com.byeboo.app.presentation.quest.review.common.other

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
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
import com.byeboo.app.presentation.quest.component.text.QuestCommonTitle
import com.byeboo.app.presentation.quest.component.type.OtherPostOption
import com.byeboo.app.presentation.quest.model.CommonReplyModel
import com.byeboo.app.presentation.quest.review.common.component.AnswerDetailTopBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@Composable
fun CommonOtherAnswerRoute(
    navigateToQuest: () -> Unit,
    paddingValues: PaddingValues,
    viewModel: CommonOtherAnswerViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val showSnackBar = LocalSnackBarTrigger.current

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is CommonAnswerSideEffect.NavigateToQuest -> navigateToQuest()
                is CommonAnswerSideEffect.ShowSnackBar -> showSnackBar(effect.snackBarType)
            }
        }
    }

    CommonOtherAnswerScreen(
        uiState = uiState,
        paddingValues = paddingValues,
        onBackClick = viewModel::onBackClicked,
        onClickMoreOptions = viewModel::onClickMoreOptions,
        onDismissBottomSheet = viewModel::onDismissBottomSheet,
        onOptionClick = { option -> viewModel.onOptionClicked(option) },
        onHeartClick = viewModel::onHeartClicked,
        onCompleteComment = viewModel::onCompleteComment,
        onCommentClick = viewModel::onCommentClick,
        onCompleteReply = viewModel::onCompleteReply,
        onDismissReplyBottomSheet = viewModel::onDismissReplyBottomSheet,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun CommonOtherAnswerScreen(
    uiState: CommonAnswerState,
    paddingValues: PaddingValues,
    onBackClick: () -> Unit,
    onClickMoreOptions: () -> Unit,
    onDismissBottomSheet: () -> Unit,
    onOptionClick: (OtherPostOption) -> Unit,
    onHeartClick: () -> Unit,
    onCompleteComment: (String) -> Unit,
    onCommentClick: (CommonReplyModel) -> Unit,
    onDismissReplyBottomSheet: () -> Unit,
    onCompleteReply: (String) -> Unit,
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
    var contentAlpha by remember { mutableStateOf(1f) }

    LaunchedEffect(Unit) {
        snapshotFlow { imeTarget.getBottom(density) > 0 }
            .distinctUntilChanged()
            .collect { visible -> isKeyboardVisible = visible }
    }

    LaunchedEffect(isKeyboardVisible) {
        if (isKeyboardVisible) {
            contentAlpha = 1f
            focusRequester.requestFocus()
        } else {
            delay(200)
            contentAlpha = 1f
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
            AnswerDetailTopBar(
                onBackClick = onBackClick,
                onClickMoreOptions = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    onClickMoreOptions()
                },
            )

            QuestCommonTitle(
                createdAt = uiState.createdAt,
                questQuestion = uiState.questQuestion,
            )

            Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))

            uiState.answer?.let { answer ->
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
                    onMoreOptionsClick = {},
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
                onCompleteComment(commentText)
                contentAlpha = 0f
                commentText = ""
                keyboardController?.hide()
                focusManager.clearFocus()
                shouldScrollToBottom = true
            },
            contentAlpha = contentAlpha,
        )
    }

    MoreOptionsBottomSheet(
        topOption = OtherPostOption.BLOCK,
        bottomOption = OtherPostOption.REPORT,
        onOptionClick = onOptionClick,
        showBottomSheet = uiState.showBottomSheet,
        onDismissRequest = onDismissBottomSheet,
    )

    uiState.selectedReply?.let { reply ->
        ReplyBottomSheet(
            showBottomSheet = uiState.showReplyBottomSheet,
            reply = reply,
            replies = uiState.replies,
            onDismissRequest = onDismissReplyBottomSheet,
            onCompleteComment = onCompleteReply,
        )
    }
}
