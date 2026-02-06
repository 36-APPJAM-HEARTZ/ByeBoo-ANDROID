package com.byeboo.app.domain.model.auth

sealed class NicknameValidationResult {
    object Empty : NicknameValidationResult()

    object Valid : NicknameValidationResult()

    object Invalid : NicknameValidationResult()
}

object NicknameValidator {
    private val regex = Regex("^[가-힣a-zA-Z0-9]{2,5}$")

    fun validate(input: String): NicknameValidationResult =
        when {
            input.isEmpty() -> NicknameValidationResult.Empty
            input.isBlank() -> NicknameValidationResult.Invalid
            regex.matches(input) -> NicknameValidationResult.Valid
            else -> NicknameValidationResult.Invalid
        }
}
