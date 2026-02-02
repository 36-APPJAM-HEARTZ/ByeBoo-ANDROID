package com.byeboo.app.presentation.quest.component.text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun CreatedText(createdAt: String) {
    val date =
        remember(createdAt) {
            LocalDate.parse(createdAt).format(DateTimeFormatter.ofPattern("yyyy. MM. dd."))
        }

    Text(
        text = "$date",
        style = ByeBooTheme.typography.body6,
        color = ByeBooTheme.colors.gray500,
    )
}
