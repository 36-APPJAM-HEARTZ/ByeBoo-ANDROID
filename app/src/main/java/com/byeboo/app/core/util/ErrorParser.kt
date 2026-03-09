package com.byeboo.app.core.util

object ErrorParser {
    fun getErrorMessage(throwable: Throwable): String {
        return if (throwable is retrofit2.HttpException) {
            try {
                val errorBody = throwable.response()?.errorBody()?.string()
                val regex = """"message"\s*:\s*"([^"]+)"""".toRegex()
                regex.find(errorBody ?: "")?.groupValues?.get(1) ?: throwable.message()
            } catch (e: Exception) {
                "알 수 없는 서버 에러가 발생했습니다."
            }
        } else {
            throwable.message ?: "네트워크 연결을 확인해 주세요."
        }
    }
}