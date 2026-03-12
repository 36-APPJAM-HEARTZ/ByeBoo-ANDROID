package com.byeboo.app.domain.model.auth

interface BadWordValidator {
    fun contains(input: String): Boolean
}
