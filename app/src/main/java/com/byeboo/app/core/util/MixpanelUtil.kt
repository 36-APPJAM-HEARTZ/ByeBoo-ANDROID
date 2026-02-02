package com.byeboo.app.core.util

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.mixpanel.android.mpmetrics.MixpanelAPI
import javax.inject.Inject
import javax.inject.Singleton
import org.json.JSONObject

@Singleton
class MixpanelUtil
@Inject
constructor() {
    private var mixpanel: MixpanelAPI? = null
    private val mainHandler = Handler(Looper.getMainLooper())

    fun initialize(
        context: Context,
        token: String
    ) {
        mixpanel = MixpanelAPI.getInstance(context, token, false)
        restoreDistinctId()
    }

    private fun restoreDistinctId() {
        mainHandler.post {
            mixpanel?.let { mp ->
                val storedDistinctId = mp.distinctId
                if (storedDistinctId != null && !isDefaultDistinctId(storedDistinctId)) {
                    mp.identify(storedDistinctId)
                    mp.people?.identify(storedDistinctId)
                }
            }
        }
    }

    private fun isDefaultDistinctId(distinctId: String): Boolean = distinctId.startsWith(
        "\$device:"
    )

    fun setDistinctId(userId: String) {
        mainHandler.post {
            mixpanel?.let { mp ->
                mp.identify(userId)
                mp.people?.identify(userId)
                mp.people?.set("user_id", userId)
            }
        }
    }

    private fun getCurrentDistinctId(): String? = mixpanel?.distinctId

    fun hasUserDistinctId(): Boolean {
        val currentId = getCurrentDistinctId()
        return currentId != null && !isDefaultDistinctId(currentId)
    }

    fun trackLogin(
        loginType: String,
        isSuccess: Boolean
    ) {
        mainHandler.post {
            mixpanel?.let { mp ->
                val props =
                    JSONObject().apply {
                        put("login_type", loginType)
                        put("is_login_complete", isSuccess)
                    }
                mp.track("login", props)
            }
        }
    }

    fun trackEvent(
        eventName: String,
        properties: Map<String, Any> = emptyMap()
    ) {
        mainHandler.post {
            mixpanel?.let { mp ->
                val props = JSONObject()
                properties.forEach { (key, value) -> props.put(key, value) }
                mp.track(eventName, props)
            }
        }
    }

    fun reset() {
        mainHandler.post {
            mixpanel?.let { mp ->
                mp.reset()
                mp.flush()
            }
        }
    }
}

object LoginType {
    const val KAKAO = "Kakao"
}
