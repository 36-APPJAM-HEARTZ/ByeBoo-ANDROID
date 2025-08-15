package com.byeboo.app.presentation.splash.termsofservice

enum class TermType(
    val content: String,
    val hasMoreText: Boolean,
    val link: String? = null
) {
    SERVICE_TERM(
        content = "(필수) 서비스 이용약관 동의",
        hasMoreText = true,
        link = "https://www.notion.so/24cab823e68d801aac95ec5d0389d192?source=copy_link"
    ),

    PRIVACY_TERM(
        content = "(필수) 개인정보 수집·이용 동의",
        hasMoreText = true,
        link = "https://www.notion.so/24cab823e68d80a19ab1fbf87d6cfbc3?source=copy_link"
    ),

    AGE_TERM(
        content = "(필수) 만 14세 이상입니다",
        hasMoreText = false,
        link = null
    )
}

