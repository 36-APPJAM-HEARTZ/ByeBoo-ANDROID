package com.byeboo.app.presentation.mypage.component

import com.google.android.material.dialog.MaterialAlertDialogBuilder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext

@Composable
fun BasicNotificationModal(
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    val context = LocalContext.current

    val currentOnDismissRequest = rememberUpdatedState(onDismissRequest)
    val currentOnConfirmClick = rememberUpdatedState(onConfirmClick)

    DisposableEffect(context) {
        val dialog = MaterialAlertDialogBuilder(context)
            .setMessage("알림 권한을 허용하시겠습니까?")
            .setPositiveButton("허용하기") { _, _ ->
                currentOnConfirmClick.value()
            }
            .setOnDismissListener {
                currentOnDismissRequest.value()
            }
            .create()

        dialog.show()

        onDispose {
            dialog.dismiss()
        }
    }
}