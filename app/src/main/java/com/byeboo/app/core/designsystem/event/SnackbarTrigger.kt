package com.byeboo.app.core.designsystem.event

import androidx.compose.runtime.staticCompositionLocalOf
import com.byeboo.app.core.designsystem.type.CustomSnackBarType

val LocalSnackBarTrigger =
    staticCompositionLocalOf<(String, CustomSnackBarType) -> Unit> {
        error("No SnackBar provided")
    }
