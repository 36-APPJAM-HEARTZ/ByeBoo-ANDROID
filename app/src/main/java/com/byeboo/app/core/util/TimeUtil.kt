package com.byeboo.app.core.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

object TimeUtil {
    private val KST_ZONE = ZoneId.of("Asia/Seoul")

    val QUEST_START_DATE: LocalDate = LocalDate.of(2026, 3, 3)

    fun getNowKst(): LocalDate = LocalDate.now(KST_ZONE)

    fun isValidDateRange(date: LocalDate): Boolean {
        val today = getNowKst()
        return !date.isBefore(QUEST_START_DATE) && !date.isAfter(today)
    }

    fun getNowLocalDateTimeKst(): LocalDateTime = LocalDateTime.now(KST_ZONE)
}
