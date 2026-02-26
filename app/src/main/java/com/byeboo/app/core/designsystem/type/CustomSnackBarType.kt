package com.byeboo.app.core.designsystem.type

import androidx.annotation.DrawableRes
import com.byeboo.app.R

enum class CustomSnackBarType(
    @DrawableRes val icon: Int,
) {
    ALERT(
        icon = R.drawable.ic_alert,
    ),

    SUCCESS(
        icon = R.drawable.ic_success,
    ),
}
