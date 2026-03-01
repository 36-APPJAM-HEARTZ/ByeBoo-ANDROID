package com.byeboo.app.core.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateUtil {
    private val defaultFormatter =
        DateTimeFormatter.ofPattern("yyyy. MM. dd.")

    fun formatToDotDate(createdAt: String): String =
        LocalDate
            .parse(createdAt)
            .format(defaultFormatter)
}
