package com.byeboo.app.presentation.mypage

data class MyPageState(
    val showLogoutModal: Boolean = false,
    val showDeleteAccountModal: Boolean = false
)

enum class ModalType {
    LOGOUT,
    DELETE_ACCOUNT
}