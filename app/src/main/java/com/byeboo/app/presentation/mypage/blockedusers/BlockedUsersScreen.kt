package com.byeboo.app.presentation.mypage.blockedusers

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.component.LoadingScreen
import com.byeboo.app.core.designsystem.event.LocalSnackBarTrigger
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.state.UiState
import com.byeboo.app.core.util.noRippleClickable
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.mypage.component.modal.BlockedUserModal
import com.byeboo.app.presentation.mypage.type.User
import kotlinx.collections.immutable.ImmutableList

@Composable
fun BlockedUsersRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    viewModel: BlockedUsersViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val showSnackBar = LocalSnackBarTrigger.current

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is BlockedUsersSideEffect.NavigateUp -> navigateUp()
                is BlockedUsersSideEffect.ShowSnackBar -> showSnackBar(effect.message)
            }
        }
    }

    when (val state = uiState) {
        is UiState.Loading -> LoadingScreen()

        is UiState.Failure -> Unit

        is UiState.Success -> {
            if (state.data.showBlockedModal) {
                BlockedUserModal(
                    onDismissRequest = viewModel::onDismissModal,
                    onNoClick = viewModel::onDismissModal,
                    onYesClick = viewModel::fetchBlockedUser,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = screenWidthDp(48.dp)),
                )
            }

            BlockedUsersScreen(
                state = state.data,
                paddingValues = paddingValues,
                onUnblockClick = { userId ->
                    viewModel.onUnblockClicked(userId)
                },
                onBackClick = viewModel::onBackClicked,
            )
        }

        else -> Unit
    }
}

@Composable
private fun BlockedUsersScreen(
    state: BlockedUsersState,
    paddingValues: PaddingValues,
    onUnblockClick: (Long) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(color = ByeBooTheme.colors.background)
                .padding(
                    top = paddingValues.calculateTopPadding() + screenHeightDp(43.dp),
                    bottom = paddingValues.calculateBottomPadding(),
                ),
    ) {
        BlockedUsersHeader(
            onBackClick = onBackClick,
        )

        BlockedUsersSection(
            userLists = state.userLists,
            onUnblockClick = onUnblockClick,
        )
    }
}

@Composable
private fun BlockedUsersHeader(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .background(color = ByeBooTheme.colors.background)
                .padding(horizontal = screenWidthDp(24.dp)),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_left),
            contentDescription = null,
            tint = ByeBooTheme.colors.gray50,
            modifier = Modifier.noRippleClickable(onClick = onBackClick),
        )

        Text(
            text = "차단 사용자 목록",
            color = ByeBooTheme.colors.white,
            style = ByeBooTheme.typography.sub1,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center),
        )
    }

    Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))
}

@Composable
private fun BlockedUsersSection(
    userLists: ImmutableList<User>,
    onUnblockClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (userLists.isEmpty()) {
        NoBlockedUser()
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(start = screenWidthDp(24.dp), top = screenHeightDp(12.dp), end = screenWidthDp(24.dp)),
    ) {
        items(
            count = userLists.size,
            key = { index -> userLists[index].id },
        ) {
            userLists.forEach { user ->
                BlockedUser(
                    blockedUsername = user.name,
                    onUnblockClick = { onUnblockClick(user.id) },
                )

                Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))
            }
        }
    }
}

@Composable
private fun NoBlockedUser() {
    Spacer(modifier = Modifier.height(screenHeightDp(220.dp)))

    Text(
        text = "차단하신 사용자가 없어요",
        color = ByeBooTheme.colors.gray400,
        style = ByeBooTheme.typography.body6,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun BlockedUser(
    blockedUsername: String,
    onUnblockClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = blockedUsername,
            color = ByeBooTheme.colors.white,
            style = ByeBooTheme.typography.body2,
        )

        Text(
            text = "해제",
            color = ByeBooTheme.colors.gray100,
            style = ByeBooTheme.typography.cap1,
            modifier =
                Modifier
                    .clip(shape = RoundedCornerShape(12.dp))
                    .border(
                        width = 1.dp,
                        color = ByeBooTheme.colors.gray800,
                        shape = RoundedCornerShape(12.dp),
                    ).background(color = ByeBooTheme.colors.whiteAlpha5)
                    .noRippleClickable(onClick = onUnblockClick)
                    .padding(horizontal = screenWidthDp(18.dp), vertical = screenHeightDp(4.dp)),
        )
    }
}
