package com.byeboo.app.core.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object TimeFormatter {
    fun formatWrittenTime(writtenAt: LocalDateTime?): String {
        if (writtenAt == null) return ""

        return runCatching {
            val nowKst = TimeUtil.getNowLocalDateTimeKst()
            val todayKst = TimeUtil.getNowKst()
            val writtenDate = writtenAt.toLocalDate()

            if (writtenDate.isEqual(todayKst)) {
                val minutes = ChronoUnit.MINUTES.between(writtenAt, nowKst)
                val hours = ChronoUnit.HOURS.between(writtenAt, nowKst)

                when {
                    minutes < 1 -> "방금 전"
                    minutes < 60 -> "${minutes}분 전"
                    hours < 24 -> "${hours}시간 전"
                    else -> writtenAt.format(DateTimeFormatter.ofPattern("yyyy. MM. dd."))
                }
            } else {
                writtenAt.format(DateTimeFormatter.ofPattern("yyyy. MM. dd."))
            }
        }.getOrDefault("")
    }
}