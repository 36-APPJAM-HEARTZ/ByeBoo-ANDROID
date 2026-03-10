package com.byeboo.app.core.designsystem.type

import androidx.annotation.DrawableRes
import com.byeboo.app.R

sealed class CustomSnackBarType(
    @DrawableRes val icon: Int,
    val message: String,
) {
    class ALERT(
        message: String = "서버에 연결할 수 없습니다. 잠시 후 시도해 주세요.",
    ) : CustomSnackBarType(
            icon = R.drawable.ic_alert,
            message = message,
        )

    class BadWord(
        message: String = "비속어나 부적절한 단어가 포함된 닉네임은 등록할 수 없어요.",
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
        val BAD_WORD = BadWord()

        fun error(exception: Throwable): ALERT {
            val msg = exception.message
            return if (msg.isNullOrBlank()) ALERT else ALERT(message = msg)
        }
    }
}
