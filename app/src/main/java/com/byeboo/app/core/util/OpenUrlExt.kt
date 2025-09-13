package com.byeboo.app.core.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import java.lang.Exception

fun openUrl(context: Context, url: String) {
    val uri = url.toUri()

    try {
        CustomTabsIntent.Builder()
            .build()
            .launchUrl(context, uri)
        return
    } catch (_: Exception) {
    }

    val actionView = Intent(Intent.ACTION_VIEW, uri).apply {
        if (context !is Activity) addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    try {
        context.startActivity(actionView)
    } catch (_: Exception) {
    }
}
