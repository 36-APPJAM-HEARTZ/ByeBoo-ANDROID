package com.byeboo.app.presentation.quest.component.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.noRippleClickable
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.quest.model.QuestTab

@Composable
fun QuestTabRow(
    selectedTab: QuestTab,
    onTabSelected: (QuestTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        QuestTabItem(
            iconRes =
                if (selectedTab == QuestTab.MY_JOURNEY) {
                    R.drawable.ic_my_journey_selected
                } else {
                    R.drawable.ic_my_journey_unselected
                },
            label = "나의 여정",
            isSelected = selectedTab == QuestTab.MY_JOURNEY,
            onTabClick = { onTabSelected(QuestTab.MY_JOURNEY) },
        )

        Spacer(modifier = Modifier.width(screenWidthDp(4.dp)))

        QuestTabItem(
            iconRes =
                if (selectedTab == QuestTab.COMMON_JOURNEY) {
                    R.drawable.ic_common_journey_selected
                } else {
                    R.drawable.ic_common_journey_unselected
                },
            label = "공통 여정",
            isSelected = selectedTab == QuestTab.COMMON_JOURNEY,
            onTabClick = { onTabSelected(QuestTab.COMMON_JOURNEY) },
        )
    }
}

@Composable
private fun QuestTabItem(
    iconRes: Int,
    label: String,
    isSelected: Boolean,
    onTabClick: () -> Unit,
) {
    val contentColor = if (isSelected) ByeBooTheme.colors.gray100 else ByeBooTheme.colors.gray600

    Column(
        modifier =
            Modifier
                .width(IntrinsicSize.Max)
                .noRippleClickable { onTabClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier =
                Modifier
                    .padding(horizontal = screenWidthDp(11.5.dp)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp),
            )

            Spacer(modifier = Modifier.width(screenWidthDp(2.dp)))

            Text(
                text = label,
                style = ByeBooTheme.typography.body2,
                color = contentColor,
                maxLines = 1,
                softWrap = false,
            )
        }

        Spacer(modifier = Modifier.height(screenHeightDp(4.dp)))

        if (isSelected) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(screenHeightDp(1.dp))
                        .background(ByeBooTheme.colors.gray300),
            )
        } else {
            Spacer(modifier = Modifier.height(screenHeightDp(1.dp)))
        }
    }
}
