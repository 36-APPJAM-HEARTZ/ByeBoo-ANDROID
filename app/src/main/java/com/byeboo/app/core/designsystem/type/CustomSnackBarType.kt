package com.byeboo.app.core.designsystem.type

import androidx.annotation.DrawableRes
import com.byeboo.app.R

sealed class CustomSnackBarType(
    @DrawableRes val icon: Int,
    val message: String,
) {
    object ALERT : CustomSnackBarType(
        icon = R.drawable.ic_alert,
        message = "서버에 연결할 수 없습니다. 잠시 후 시도해 주세요.",
    )

    class SUCCESS(
        message: String,
    ) : CustomSnackBarType(
            icon = R.drawable.ic_success,
            message = message,
        )
}
