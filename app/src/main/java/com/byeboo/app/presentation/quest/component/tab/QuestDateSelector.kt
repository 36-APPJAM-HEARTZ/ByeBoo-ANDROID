package com.byeboo.app.presentation.quest.component.tab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenWidthDp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun QuestDateSelector(
    selectedDate: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val today = LocalDate.now()
    val isToday = selectedDate.isEqual(today)

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = { onDateChange(selectedDate.minusDays(1)) }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_date_left),
                contentDescription = "이전 날짜",
                tint = Color.Unspecified,
            )
        }

        Spacer(modifier = Modifier.width(screenWidthDp(20.dp)))

        Text(
            text = selectedDate.format(DateTimeFormatter.ofPattern("M월 d일")),
            style = ByeBooTheme.typography.body2,
            color = ByeBooTheme.colors.gray50,
        )

        Spacer(modifier = Modifier.width(screenWidthDp(20.dp)))

        IconButton(
            onClick = { onDateChange(selectedDate.plusDays(1)) },
            enabled = !isToday,
        ) {
            Icon(
                painter =
                    painterResource(
                        id =
                            if (isToday) {
                                R.drawable.ic_date_right_disabled
                            } else {
                                R.drawable.ic_date_right_enabled
                            },
                    ),
                contentDescription = "다음 날짜",
                tint = Color.Unspecified,
            )
        }
    }
}
