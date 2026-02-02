package com.byeboo.app.presentation.quest.util

import java.time.Duration
import java.time.Instant
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object QuestCountdownTimer {
    fun countdownFlow(
        openAt: Instant,
        now: Instant
    ): Flow<Long> =
        flow {
            val totalInitialRemainingSeconds =
                Duration.between(now, openAt).seconds.coerceAtLeast(0)

            val monotonicStartRealtimeMillis = android.os.SystemClock.elapsedRealtime()

            var lastEmittedMinuteValue = Long.MIN_VALUE

            while (true) {
                val secondsPassedSinceStart =
                    (android.os.SystemClock.elapsedRealtime() - monotonicStartRealtimeMillis) / 1000L

                val currentRemainingSeconds =
                    (totalInitialRemainingSeconds - secondsPassedSinceStart).coerceAtLeast(0)

                val minutesToDisplay =
                    if (currentRemainingSeconds < 60) 0L else currentRemainingSeconds / 60

                if (minutesToDisplay != lastEmittedMinuteValue) {
                    lastEmittedMinuteValue = minutesToDisplay
                    emit(minutesToDisplay)
                }
                if (currentRemainingSeconds == 0L) break

                val secondsIntoCurrentMinute = (currentRemainingSeconds % 60).toInt()
                val delayMillisUntilNextTick =
                    if (currentRemainingSeconds > 60) {
                        if (secondsIntoCurrentMinute == 0) 1000L else secondsIntoCurrentMinute * 1000L
                    } else {
                        1000L
                    }

                delay(delayMillisUntilNextTick)
            }
        }
}
