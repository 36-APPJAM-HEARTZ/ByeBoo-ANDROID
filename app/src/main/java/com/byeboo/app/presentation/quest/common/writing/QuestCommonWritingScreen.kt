package com.byeboo.app.presentation.quest.common.writing

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.byeboo.app.core.designsystem.event.LocalSnackBarTrigger
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.addFocusCleaner
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.quest.component.modal.QuestCompleteModal
import com.byeboo.app.presentation.quest.component.modal.QuestQuitModal
import com.byeboo.app.presentation.quest.component.text.QuestWritingFooter
import com.byeboo.app.presentation.quest.component.text.QuestWritingTitle
import com.byeboo.app.presentation.quest.component.text.textfield.QuestTextField
import com.byeboo.app.presentation.quest.component.topbar.QuestWritingTopbar
import kotlinx.coroutines.flow.collectLatest

@Composable
fun QuestCommonRoute(
    navigateToQuestFromComplete: () -> Unit,
    navigateUp: () -> Unit,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: QuestCommonWritingViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val showSnackBar = LocalSnackBarTrigger.current

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is QuestCommonSideEffect.NavigateToQuest -> navigateToQuestFromComplete()
                is QuestCommonSideEffect.NavigateToUp -> navigateUp()
                is QuestCommonSideEffect.ShowSnackBar -> showSnackBar(effect.snackBarType)
            }
        }
    }

    if (uiState.showQuitModal) {
        QuestQuitModal(
            onDismissRequest = viewModel::onDismissQuitModal,
            stayButton = viewModel::onDismissQuitModal,
            quitButton = {
                viewModel.onDismissQuitModal()
                viewModel.onQuitClicked()
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = screenWidthDp(48.dp)),
        )
    }

    if (uiState.showCompleteModal) {
        QuestCompleteModal(
            onDismissRequest = viewModel::onDismissCompleteModal,
            onNoClick = viewModel::onDismissCompleteModal,
            onYesClick = viewModel::onSaveClicked,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = screenWidthDp(48.dp)),
        )
    }

    BackHandler { viewModel.onBackClicked() }

    QuestCommonScreen(
        uiState = uiState,
        paddingValues = paddingValues,
        onBackClick = viewModel::onBackClicked,
        onCompleteClick = viewModel::onCompleteClicked,
        onUpdateContent = viewModel::updateContent,
        modifier = modifier,
    )
}

@Composable
private fun QuestCommonScreen(
    uiState: QuestCommonState,
    paddingValues: PaddingValues,
    onBackClick: () -> Unit,
    onCompleteClick: () -> Unit,
    onUpdateContent: (Boolean, String) -> Unit,
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
        QuestWritingTopbar(
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
                question = uiState.question,
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
            modifier =
                Modifier
                    .padding(horizontal = screenWidthDp(24.dp)),
        )
    }
}
