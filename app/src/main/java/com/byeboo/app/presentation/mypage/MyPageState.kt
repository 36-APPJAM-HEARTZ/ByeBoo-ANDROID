package com.byeboo.app.presentation.mypage

data class MyPageState(
    val nickname: String = "",
    val showLogoutModal: Boolean = false,
    val showDeleteAccountModal: Boolean = false
)

enum class ModalType {
    LOGOUT,
    DELETE_ACCOUNT
}

sealed interface MyPageSideEffect {
    data class OpenUrl(val url: String) : MyPageSideEffect
    data object NavigateToEditProfile : MyPageSideEffect
    data object NavigateToOffboardingCompletedJourney : MyPageSideEffect
    data object NavigateToTutorial : MyPageSideEffect
}
