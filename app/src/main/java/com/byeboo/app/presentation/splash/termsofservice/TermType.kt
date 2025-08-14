package com.byeboo.app.presentation.splash.termsofservice

enum class TermType(
    val content: String,
    val hasMoreText: Boolean,
) {
    SERVICE_TERM(
        content = "(필수) 서비스 이용약관 동의",
        hasMoreText = true
    ),

    PRIVACY_TERM(
        content = "(필수) 개인정보 수집·이용 동의",
        hasMoreText = true
    ),

    AGE_TERM(
        content = "(필수) 만 14세 이상입니다",
        hasMoreText = false
    )
}

