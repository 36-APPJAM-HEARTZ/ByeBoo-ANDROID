package com.byeboo.app.domain.model.home

enum class HomeStatus {
    INITIAL_START,
    TODAY_INCOMPLETE,
    TODAY_COMPLETE,
    JOURNEY_COMPLETE,
    ;

    companion object {
        fun from(status: String?): HomeStatus =
            when (status) {
                "INITIAL_START_STATUS" -> INITIAL_START
                "TODAY_NOT_COMPLETED_STATUS" -> TODAY_INCOMPLETE
                "TODAY_COMPLETED_STATUS" -> TODAY_COMPLETE
                "JOURNEY_COMPLETED_STATUS" -> JOURNEY_COMPLETE
                else -> INITIAL_START
            }
    }
}
