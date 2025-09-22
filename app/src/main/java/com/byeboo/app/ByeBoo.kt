package com.byeboo.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.byeboo.app.core.util.MixpanelUtil
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

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
}