package com.byeboo.app.core.util

fun formatHourMinutes(totalMinutes: Long): String {
    val h = (totalMinutes / 60).coerceAtLeast(0)
    val m = (totalMinutes % 60).coerceAtLeast(0)
    return "%02d:%02d".format(h, m)
}
