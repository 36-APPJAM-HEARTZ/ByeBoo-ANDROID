package com.byeboo.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.byeboo.app.core.util.MixpanelUtil
import com.byeboo.app.fcm.ByebooMessagingService
import com.byeboo.app.fcm.ByebooNotificationHandler
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import timber.log.Timber

@HiltAndroidApp
class ByeBoo : Application() {

    @Inject
    lateinit var mixpanelUtil: MixpanelUtil

    override fun onCreate() {
        super.onCreate()

        initTimber()
        setNightMode()
        initKakaoSdk()
        initMixpanel()
        initNotificationChannel()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    private fun setNightMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    private fun initKakaoSdk() {
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }

    private fun initMixpanel() {
        mixpanelUtil.initialize(this, BuildConfig.MIXPANEL_TOKEN)
    }

    private fun initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                ByebooNotificationHandler.CHANNEL_ID,
                ByebooNotificationHandler.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}
