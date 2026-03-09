package com.byeboo.app.core.designsystem.type

import androidx.annotation.DrawableRes
import com.byeboo.app.R

sealed class CustomSnackBarType(
    @DrawableRes val icon: Int,
    val message: String,
) {
    class ALERT(
        message: String = "서버에 연결할 수 없습니다. 잠시 후 시도해 주세요."
    ) : CustomSnackBarType(
        icon = R.drawable.ic_alert,
        message = message,
    )

    class SUCCESS(
        message: String,
    ) : CustomSnackBarType(
        icon = R.drawable.ic_success,
        message = message,
    )

    companion object {
        val ALERT = ALERT()

        fun error(exception: Throwable): ALERT {
            val msg = exception.message
            return if (msg.isNullOrBlank()) ALERT else ALERT(message = msg)
        }
    }
}
