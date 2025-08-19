package com.byeboo.app.presentation.splash.termsofservice

import com.byeboo.app.BuildConfig

enum class TermType(
    val content: String,
    val hasMoreText: Boolean,
    val link: String? = null
) {
    SERVICE_TERM(
        content = "(필수) 서비스 이용약관 동의",
        hasMoreText = true,
        link = BuildConfig.BYEBOO_TERMS_OF_SERVICE
    ),

    PRIVACY_TERM(
        content = "(필수) 개인정보 수집·이용 동의",
        hasMoreText = true,
        link = BuildConfig.BYEBOO_PRIVACY_POLICY
    ),

    AGE_TERM(
        content = "(필수) 만 14세 이상입니다",
        hasMoreText = false,
        link = null
    )
}

