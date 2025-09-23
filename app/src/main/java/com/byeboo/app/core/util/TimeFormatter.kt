package com.byeboo.app.core.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatHourMinutes(totalMinutes: Long): String {
    val h = (totalMinutes / 60).coerceAtLeast(0)
    val m = (totalMinutes % 60).coerceAtLeast(0)
    return "%02d:%02d".format(h, m)
}

fun getFormattedDate(timestamp: Long = System.currentTimeMillis()): String {
    val sdf = SimpleDateFormat("yyyy. MM. dd a hh:mm:ss", Locale.KOREA)
    return sdf.format(Date(timestamp))
}
