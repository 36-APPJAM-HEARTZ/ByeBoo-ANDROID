package com.byeboo.app.core.designsystem.event

import androidx.compose.runtime.staticCompositionLocalOf
import com.byeboo.app.core.designsystem.type.CustomSnackBarType

val LocalSnackBarTrigger =
    staticCompositionLocalOf<(CustomSnackBarType) -> Unit> {
        error("No SnackBar provided")
    }
