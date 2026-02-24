package com.byeboo.app.presentation.quest.review.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.presentation.quest.component.card.CommonAnswerItem
import com.byeboo.app.presentation.quest.component.text.QuestTitle
import com.byeboo.app.presentation.quest.model.CommonAnswerModel

@Composable
fun CommonAnswerRoute(
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: CommonAnswerViewModel = hiltViewModel(),
    ) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CommonAnswerScreen(
        uiState = uiState,
        paddingValues = paddingValues,
        modifier = modifier
    )
}

@Composable
private fun CommonAnswerScreen(
    uiState: CommonAnswerState,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ByeBooTheme.colors.background)
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp)
            .padding(
                top = paddingValues.calculateTopPadding() + screenHeightDp(43.dp),
                bottom = paddingValues.calculateBottomPadding(),
            )
    ) {
        CommonAnswerTopBar(
            modifier = modifier
        )

        QuestTitle(
            stepNumber = 2,
            questNumber = 10,
            createdAt = "2025-06-01",
            questQuestion = "그 사람이 싫어하기에 내가 포기해야만 했던 일은 무엇일까?"
        )

        Spacer(modifier = Modifier.padding(bottom = screenHeightDp(10.dp)))

        CommonAnswerItem(
            answer = uiState.answer,
            isExpanded = true
        )
    }

}

@Composable
private fun CommonAnswerTopBar(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_left),
            contentDescription = null,
            tint = ByeBooTheme.colors.gray50
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_overflow_menu),
            contentDescription = null,
            tint = ByeBooTheme.colors.white,
        )

        Spacer(modifier = Modifier.padding(bottom = screenHeightDp(16.dp)))
    }
}


