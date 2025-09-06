package com.byeboo.app.domain.model.offboarding

enum class OffboardingJourneyType(val serverKey: String, val displayName: String) {
    FACE_EMOTION("FACE_EMOTION", "감정 직면"),
    PROCESS_EMOTION("PROCESS_EMOTION", "감정 정리");

    companion object {
        fun fromDisplayName(displayName: String) =
            entries.firstOrNull { it.displayName == displayName } ?: FACE_EMOTION
    }
}
