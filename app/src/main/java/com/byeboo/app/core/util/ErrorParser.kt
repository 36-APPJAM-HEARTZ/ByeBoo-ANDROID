package com.byeboo.app.core.util

import org.json.JSONObject
import retrofit2.HttpException

object ErrorParser {
    fun getErrorMessage(throwable: Throwable): String {
        if (throwable !is HttpException) {
            return throwable.message ?: "네트워크 연결을 확인해 주세요."
        }

        return runCatching {
            val errorBody =
                throwable
                    .response()
                    ?.errorBody()
                    ?.string()
                    .orEmpty()
            JSONObject(errorBody).optString("message")
        }.getOrNull()?.takeIf { it.isNotBlank() } ?: "알 수 없는 서버 에러가 발생했습니다."
    }
}
