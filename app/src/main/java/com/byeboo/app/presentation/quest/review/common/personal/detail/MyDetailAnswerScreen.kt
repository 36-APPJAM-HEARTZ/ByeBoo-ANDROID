package com.byeboo.app.presentation.quest.review.common.personal.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.quest.component.bottomsheet.MoreOptionsBottomSheet
import com.byeboo.app.presentation.quest.component.modal.QuestDeleteModal
import com.byeboo.app.presentation.quest.component.modal.QuestQuitModal
import com.byeboo.app.presentation.quest.component.text.QuestCommonTitle
import com.byeboo.app.presentation.quest.component.type.MyPostOption
import com.byeboo.app.presentation.quest.review.common.component.AnswerDetailTopBar
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MyAnswerDetailRoute(
    navigateUp: () -> Unit,
    navigateToQuestMyAnswers: () -> Unit,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: MyDetailAnswerViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MyAnswerDetailScreen(
        uiState = uiState,
        paddingValues = paddingValues,
        onBackClick = viewModel::onBackClicked,
        onClickMoreOptions = viewModel::onClickMoreOptions,
        onDismissBottomSheet = viewModel::onDismissBottomSheet,
        onOptionClick = { option -> viewModel.onOptionClicked(option) },
        modifier = modifier,
    )

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is MyDetailAnswerSideEffect.NavigateUp -> navigateUp()
                is MyDetailAnswerSideEffect.NavigateToQuestMyAnswers -> navigateToQuestMyAnswers()
            }
        }
    }

    if (uiState.showDeleteModal) {
        QuestDeleteModal(
            onDismissRequest = viewModel::onDismissDeleteModal,
            onNoClick = viewModel::onDismissDeleteModal,
            onYesClick = {
                viewModel.onDismissDeleteModal()
                viewModel.onQuestDeleteClicked()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = screenWidthDp(48.dp))
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyAnswerDetailScreen(
    uiState: MyDetailAnswerState,
    paddingValues: PaddingValues,
    onBackClick: () -> Unit,
    onClickMoreOptions: () -> Unit,
    onDismissBottomSheet: () -> Unit,
    onOptionClick: (MyPostOption) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(ByeBooTheme.colors.background)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
                .padding(
                    top = paddingValues.calculateTopPadding() + screenHeightDp(43.dp),
                    bottom = paddingValues.calculateBottomPadding(),
                ),
    ) {
        AnswerDetailTopBar(
            onBackClick = onBackClick,
            onClickMoreOptions = onClickMoreOptions,
        )

        QuestCommonTitle(
            createdAt = uiState.answer.writtenAt,
            questQuestion = uiState.answer.question,
        )

        Spacer(modifier = Modifier.height(screenHeightDp(10.dp)))

        MyAnswerContent(
            content = uiState.answer.content,
        )
    }

    MoreOptionsBottomSheet(
        topOption = MyPostOption.EDIT,
        bottomOption = MyPostOption.DELETE,
        onOptionClick = onOptionClick,
        showBottomSheet = uiState.showBottomSheet,
        onDismissRequest = onDismissBottomSheet,
    )
}

@Composable
private fun MyAnswerContent(
    content: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .background(
                    color = ByeBooTheme.colors.whiteAlpha5,
                    shape = RoundedCornerShape(12.dp),
                ).padding(
                    horizontal = screenWidthDp(24.dp),
                    vertical = screenHeightDp(18.dp),
                ),
    ) {
        Text(
            text = content,
            color = ByeBooTheme.colors.gray100,
            style = ByeBooTheme.typography.body3,
        )
    }
}
