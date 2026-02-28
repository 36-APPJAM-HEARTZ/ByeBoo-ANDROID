package com.byeboo.app.core.designsystem.component.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import com.byeboo.app.core.designsystem.type.CustomSnackBarType

class CustomSnackBarVisuals(
    val type: CustomSnackBarType,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
) : SnackbarVisuals {
    override val message: String = type.message
}
