package com.byeboo.app.core.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateUtil {
    private val defaultFormatter =
        DateTimeFormatter.ofPattern("yyyy. MM. dd.")

    fun formatToDotDate(date: String): String {
        return runCatching {
            if (date.isBlank()) return ""
            LocalDate.parse(date).format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
        }.getOrElse { "" }
    }
}
