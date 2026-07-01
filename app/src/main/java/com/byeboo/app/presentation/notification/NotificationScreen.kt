package com.byeboo.app.presentation.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.component.topbar.ByeBooTopbar
import com.byeboo.app.core.designsystem.event.LocalSnackBarTrigger
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.noRippleClickable
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.notification.component.NotificationCard
import com.byeboo.app.presentation.notification.model.NotificationUiModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun NotificationRoute(
    navigateToHome: () -> Unit,
    navigateToDeepLink: (String) -> Unit,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: NotificationViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val showSnackBar = LocalSnackBarTrigger.current


    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->

            when (effect) {
                is NotificationSideEffect.NavigateToHome -> navigateToHome()
                is NotificationSideEffect.NavigateToDeepLink -> {
                    navigateToDeepLink(effect.landingUrl)
                }
                is NotificationSideEffect.ShowSnackBar -> showSnackBar(effect.snackBarType)

            }
        }
    }


    LifecycleResumeEffect(Unit) {
        viewModel.fetchNotificationList()
        onPauseOrDispose {  }
    }

    NotificationScreen(
        uiState = uiState,
        paddingValues = paddingValues,
        onBackClick = viewModel::onBackClicked,
        onAllReadClick = viewModel::onAllNotificationsReadClicked,
        onNotificationClick = viewModel::onNotificationClicked,
        modifier = modifier,
    )
}

@Composable
private fun NotificationScreen(
    uiState: NotificationUiState,
    onBackClick: () -> Unit,
    onAllReadClick: () -> Unit,
    onNotificationClick: (NotificationUiModel) -> Unit,
    paddingValues: PaddingValues,
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
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        NotificationTopBar(onBackClick = onBackClick)

        NotificationListHeader(onAllReadClick = onAllReadClick)

        Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))

        NotificationSection(
            modifier = Modifier.weight(1f),
            uiState = uiState,
            onNotificationClick = onNotificationClick,
        )
    }
}

@Composable
private fun NotificationTopBar(onBackClick: () -> Unit) {
    ByeBooTopbar(
        navigationIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_left),
                contentDescription = null,
                tint = ByeBooTheme.colors.gray50,
                modifier =
                    Modifier
                        .padding(horizontal = screenWidthDp(24.dp))
                        .noRippleClickable(onClick = onBackClick),
            )
        },
        title = "알림",
    )
}

@Composable
private fun NotificationListHeader(onAllReadClick: () -> Unit) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = screenWidthDp(24.dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "최근 30일까지만 보여요",
            color = ByeBooTheme.colors.gray300,
            style = ByeBooTheme.typography.cap1,
        )

        Text(
            text = "모두 읽기",
            color = ByeBooTheme.colors.gray300,
            style = ByeBooTheme.typography.cap1,
            modifier =
                Modifier.noRippleClickable(
                    onClick = onAllReadClick,
                ),
        )
    }
}

@Composable
private fun NotificationSection(
    uiState: NotificationUiState,
    onNotificationClick: (NotificationUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiState.notificationList.isNotEmpty()) {
        LazyColumn(
            contentPadding =
                PaddingValues(
                    start = screenWidthDp(24.dp),
                    end = screenWidthDp(24.dp),
                    bottom = screenHeightDp(24.dp),
                ),
            verticalArrangement = Arrangement.spacedBy(screenHeightDp(20.dp)),
        ) {
            items(
                items = uiState.notificationList,
                key = { it.notificationId },
            ) { notification ->
                NotificationCard(
                    notification = notification,
                    onClick = { onNotificationClick(notification) },
                )
            }
        }
    } else {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.weight(171f))

            Text(
                text = "아직 도착한 알림이 없어요!",
                color = ByeBooTheme.colors.gray400,
                style = ByeBooTheme.typography.body6,
            )

            Spacer(modifier = Modifier.weight(361f))
        }
    }
}
