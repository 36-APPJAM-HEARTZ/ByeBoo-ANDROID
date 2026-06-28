package com.byeboo.app.presentation.quest.component.bottomsheet

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imeAnimationTarget
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.component.topbar.ByeBooTopbar
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.noRippleClickable
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.quest.component.card.CommonReplyItem
import com.byeboo.app.presentation.quest.component.input.CommentInputBar
import com.byeboo.app.presentation.quest.model.CommonReplyModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ReplyBottomSheet(
    showBottomSheet: Boolean,
    reply: CommonReplyModel,
    replies: ImmutableList<CommonReplyModel>,
    onDismissRequest: () -> Unit,
    onCompleteComment: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (showBottomSheet) {
        val sheetState =
            rememberModalBottomSheetState(
                skipPartiallyExpanded = true,
            )

        val disableNestedScrollConnection =
            remember {
                object : NestedScrollConnection {
                    override fun onPreScroll(
                        available: Offset,
                        source: NestedScrollSource,
                    ): Offset = Offset.Zero

                    override fun onPostScroll(
                        consumed: Offset,
                        available: Offset,
                        source: NestedScrollSource,
                    ): Offset = available

                    override suspend fun onPreFling(available: Velocity): Velocity = Velocity.Zero

                    override suspend fun onPostFling(
                        consumed: Velocity,
                        available: Velocity,
                    ): Velocity = available
                }
            }

        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current
        val clearKeyboardFocus = {
            focusManager.clearFocus(force = true)
            keyboardController?.hide()
        }
        val coroutineScope = rememberCoroutineScope()
        val hideSheet = {
            clearKeyboardFocus()
            coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) onDismissRequest()
            }
        }

        ModalBottomSheet(
            onDismissRequest = { hideSheet() },
            modifier =
                modifier
                    .fillMaxHeight()
                    .statusBarsPadding()
                    .padding(top = screenHeightDp(20.dp)),
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
            containerColor = ByeBooTheme.colors.background,
            scrimColor = ByeBooTheme.colors.blackAlpha80,
            dragHandle = null,
            contentWindowInsets = { WindowInsets(0.dp, 0.dp, 0.dp, 0.dp) },
            properties =
                ModalBottomSheetProperties(
                    shouldDismissOnBackPress = true,
                ),
        ) {
            var commentText by remember { mutableStateOf("") }
            val maxLength = 500
            val isCompleteEnabled = commentText.isNotEmpty()

            val density = LocalDensity.current

            var isKeyboardVisible by remember { mutableStateOf(false) }

            val imeTarget = WindowInsets.imeAnimationTarget
            val navBarInsets = WindowInsets.navigationBars

            val navBottom = with(density) { navBarInsets.getBottom(density).toDp() }

            val targetPadding = if (isKeyboardVisible) 0.dp else navBottom

            val inputBottomPadding by animateDpAsState(
                targetValue = targetPadding,
                animationSpec = tween(durationMillis = 200),
                label = "inputBottomPadding",
            )

            val view = LocalView.current

            val focusRequester = remember { FocusRequester() }
            val scrollState = rememberScrollState()

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

            LaunchedEffect(replies) {
                if (replies.isNotEmpty()) {
                    coroutineScope.launch {
                        scrollState.animateScrollTo(scrollState.maxValue)
                    }
                }
            }

            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(bottom = inputBottomPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ByeBooDragHandle(
                    modifier = Modifier.noRippleClickable { hideSheet() },
                )

                Spacer(modifier = Modifier.height(screenHeightDp(8.dp)))

                Box(modifier = Modifier.padding(horizontal = screenWidthDp(24.dp))) {
                    ByeBooTopbar(
                        title = "답글",
                        navigationIcon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_left),
                                contentDescription = null,
                                tint = ByeBooTheme.colors.gray50,
                                modifier = Modifier.noRippleClickable { hideSheet() },
                            )
                        },
                    )
                }

                Column(
                    modifier =
                        Modifier
                            .weight(1f)
                            .nestedScroll(disableNestedScrollConnection)
                            .verticalScroll(scrollState)
                            .padding(horizontal = screenWidthDp(24.dp)),
                ) {
                    CommonReplyItem(
                        reply = reply,
                        onMoreOptionsClick = {},
                        onCommentClick = {},
                    )

                    Spacer(modifier = Modifier.height(screenHeightDp(8.dp)))

                    replies.forEach { replyItem ->
                        CommonReplyItem(
                            reply = replyItem,
                            onMoreOptionsClick = {},
                            onCommentClick = {},
                            isReply = true,
                        )
                    }
                }

                CommentInputBar(
                    commentText = commentText,
                    isKeyboardVisible = isKeyboardVisible,
                    focusRequester = focusRequester,
                    isCompleteEnabled = isCompleteEnabled,
                    maxLength = maxLength,
                    onTextChange = { if (it.length <= maxLength) commentText = it },
                    onCompleteClick = {
                        onCompleteComment(commentText)
                        commentText = ""
                        ViewCompat.getWindowInsetsController(view)?.hide(WindowInsetsCompat.Type.ime())
                    },
                    placeholder = "답글로 위로를 남겨보세요.",
                )
            }
        }
    }
}
