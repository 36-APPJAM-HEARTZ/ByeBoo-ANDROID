package com.byeboo.app.core.designsystem.component.backhandler

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

@Composable
fun ByeBooBackHandler(message: String = "뒤로가기를 한 번 더 누르면 앱이 종료됩니다") {
    val context = LocalContext.current

    CloseBackHandler(
        context = context,
        enabled = true,
        exitDelayMillis = 3000L,
        onShowSnackBar = {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        },
    )
}

@Composable
fun CloseBackHandler(
    context: Context,
    enabled: Boolean = true,
    exitDelayMillis: Long = 3000L,
    onShowSnackBar: () -> Unit = {},
) {
    var backPressedTime by remember {
        mutableLongStateOf(0L)
    }

    BackHandler(enabled = enabled) {
        if (System.currentTimeMillis() - backPressedTime <= exitDelayMillis) {
            (context as Activity).finish()
        } else {
            onShowSnackBar()
        }
        backPressedTime = System.currentTimeMillis()
    }
}
