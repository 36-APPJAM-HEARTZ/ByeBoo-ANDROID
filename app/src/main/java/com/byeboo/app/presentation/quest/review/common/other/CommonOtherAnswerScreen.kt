package com.byeboo.app.presentation.quest.review.common.other

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.byeboo.app.core.designsystem.event.LocalSnackBarTrigger
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.noRippleClickable
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.quest.component.bottomsheet.MoreOptionsBottomSheet
import com.byeboo.app.presentation.quest.component.card.CommonAnswerItem
import com.byeboo.app.presentation.quest.component.text.QuestCommonTitle
import com.byeboo.app.presentation.quest.component.type.OtherPostOption
import com.byeboo.app.presentation.quest.review.common.component.AnswerDetailTopBar
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.runtime.rememberUpdatedState

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
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CommonOtherAnswerScreen(
    uiState: CommonAnswerState,
    paddingValues: PaddingValues,
    onBackClick: () -> Unit,
    onClickMoreOptions: () -> Unit,
    onDismissBottomSheet: () -> Unit,
    onOptionClick: (OtherPostOption) -> Unit,
    onHeartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    var commentText by remember { mutableStateOf("") }
    val maxLength = 500
    val isCompleteEnabled = commentText.isNotEmpty()

    val density = LocalDensity.current
    val imeBottomPx by rememberUpdatedState(WindowInsets.ime.getBottom(density))
    val isKeyboardVisible = imeBottomPx > 0

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ByeBooTheme.colors.background)
            .padding(
                top = paddingValues.calculateTopPadding() + screenHeightDp(43.dp),
                bottom = if (isKeyboardVisible) 0.dp else paddingValues.calculateBottomPadding(),
            )
            .imePadding(),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
                .padding(bottom = screenHeightDp(16.dp)),
        ) {
            AnswerDetailTopBar(
                onBackClick = onBackClick,
                onClickMoreOptions = onClickMoreOptions,
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
    }

        CommentInputBar(
            commentText = commentText,
            isKeyboardVisible = isKeyboardVisible,
            isCompleteEnabled = isCompleteEnabled,
            maxLength = maxLength,
            onTextChange = { newText ->
                if (newText.length <= maxLength) commentText = newText
            },
            onCompleteClick = {
                // TODO: API 연동 시 처리
            },
        )
    }

    MoreOptionsBottomSheet(
        topOption = OtherPostOption.BLOCK,
        bottomOption = OtherPostOption.REPORT,
        onOptionClick = onOptionClick,
        showBottomSheet = uiState.showBottomSheet,
        onDismissRequest = onDismissBottomSheet,
    )
}

@Composable
private fun CommentInputBar(
    commentText: String,
    isKeyboardVisible: Boolean,
    isCompleteEnabled: Boolean,
    maxLength: Int,
    onTextChange: (String) -> Unit,
    onCompleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val maxLines = if (isKeyboardVisible) 5 else 1
    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(ByeBooTheme.colors.background),
    ) {
        HorizontalDivider(
            thickness = 1.dp,
            color = ByeBooTheme.colors.gray800,
        )
        if (isKeyboardVisible) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ByeBooTheme.colors.background)
                    .padding(horizontal = screenWidthDp(24.dp), vertical = screenHeightDp(8.dp)),
            ) {
                BasicTextField(
                    value = commentText,
                    onValueChange = onTextChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = screenHeightDp(42.dp))
                        .wrapContentHeight(align = Alignment.Top, unbounded = true)
                        .focusRequester(focusRequester),
                    textStyle = ByeBooTheme.typography.body6.copy(
                        color = ByeBooTheme.colors.gray100
                    ),
                    cursorBrush = SolidColor(ByeBooTheme.colors.white),
                    minLines = 1,
                    maxLines = maxLines,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Default,
                    ),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.TopStart,
                        ) {
                            if (commentText.isEmpty()) {
                                Text(
                                    text = "댓글로 위로를 남겨보세요.",
                                    style = ByeBooTheme.typography.body6,
                                    color = ByeBooTheme.colors.gray600,
                                )
                            }
                            innerTextField()
                        }
                    },
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = screenHeightDp(4.dp),
                        ),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "${commentText.length}/$maxLength",
                        style = ByeBooTheme.typography.cap2,
                        color = ByeBooTheme.colors.gray400,
                    )

                    Spacer(modifier = Modifier.width(screenWidthDp(12.dp)))

                    Text(
                        text = "완료",
                        style = ByeBooTheme.typography.body2,
                        color = if (isCompleteEnabled) {
                            ByeBooTheme.colors.primary300
                        } else {
                            ByeBooTheme.colors.gray600
                        },
                        modifier = Modifier.then(
                            if (isCompleteEnabled) {
                                Modifier.noRippleClickable(onClick = onCompleteClick)
                            } else Modifier
                        ),
                    )
                }
            }

            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = screenWidthDp(24.dp))
                    .padding(
                        top = screenHeightDp(8.dp),
                        bottom = screenHeightDp(2.dp),
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(screenWidthDp(20.dp)),
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(ByeBooTheme.colors.whiteAlpha5)
                        .padding(
                            horizontal = screenWidthDp(12.dp),
                            vertical = screenHeightDp(9.5.dp),
                        )
                        .noRippleClickable { focusRequester.requestFocus() },
                ) {
                    if (commentText.isEmpty()) {
                        Text(
                            text = "댓글로 위로를 남겨보세요.",
                            style = ByeBooTheme.typography.body6,
                            color = ByeBooTheme.colors.gray600,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    } else {
                        Text(
                            text = commentText,
                            style = ByeBooTheme.typography.body6.copy(color = ByeBooTheme.colors.gray100),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    // focusRequester용 숨겨진 BasicTextField
                    BasicTextField(
                        value = commentText,
                        onValueChange = onTextChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .then(Modifier.height(0.dp)),  // 화면에 안 보이게
                        textStyle = ByeBooTheme.typography.body6.copy(
                            color = ByeBooTheme.colors.gray100,
                        ),
                        cursorBrush = SolidColor(ByeBooTheme.colors.white),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Default,
                        ),
                    )
                }

                Text(
                    text = "완료",
                    style = ByeBooTheme.typography.body2,
                    color = if (isCompleteEnabled) {
                        ByeBooTheme.colors.primary300
                    } else {
                        ByeBooTheme.colors.gray600
                    },
                    modifier = Modifier.then(
                        if (isCompleteEnabled) {
                            Modifier.noRippleClickable(onClick = onCompleteClick)
                        } else Modifier
                    ),
                )
            }
        }
    }
}