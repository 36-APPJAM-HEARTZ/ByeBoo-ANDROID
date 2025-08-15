package com.byeboo.app.core.util

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri

fun openUrl(context: Context, url: String) {
    CustomTabsIntent.Builder()
        .build()
        .launchUrl(context, url.toUri())
}