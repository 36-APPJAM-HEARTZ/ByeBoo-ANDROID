package com.byeboo.app.presentation.quest.review.common.personal

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.quest.component.bottomsheet.MoreOptionsBottomSheet
import com.byeboo.app.presentation.quest.component.text.QuestTitle
import com.byeboo.app.presentation.quest.component.type.MyPostOption
import com.byeboo.app.presentation.quest.review.common.component.AnswerDetailTopBar

@Composable
fun MyAnswerDetailRoute(
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: MyAnswerViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MyAnswerDetailScreen(
        uiState = uiState,
        paddingValues = paddingValues,
        onClickMoreOptions = viewModel::onClickMoreOptions,
        onDismissBottomSheet = viewModel::onDismissBottomSheet,
        onOptionClick = { option -> viewModel.onOptionClicked(option) },
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyAnswerDetailScreen(
    uiState: MyAnswerState,
    paddingValues: PaddingValues,
    onClickMoreOptions: () -> Unit,
    onDismissBottomSheet: () -> Unit,
    onOptionClick: (MyPostOption) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    val answerState = uiState.answers.first()

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
            onClickMoreOptions = onClickMoreOptions,
        )

        // Todo: QuestWritingTitle 컴포넌트로 교체
        QuestTitle(
            stepNumber = 2,
            questNumber = 10,
            createdAt = answerState.writtenAt,
            questQuestion = "그 사람이 싫어하기에 내가 포기해야만 했던 일은 무엇일까?",
        )

        Spacer(modifier = Modifier.height(screenHeightDp(10.dp)))

        MyAnswerContent(
            content = answerState.content,
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
