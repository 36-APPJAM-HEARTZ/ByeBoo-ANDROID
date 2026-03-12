package com.byeboo.app.core.model.quest

import kotlinx.serialization.Serializable

@Serializable
enum class QuestType(
    val questStyle: String,
    val journeyName: String,
    val journeyType: String,
) {
    RECORDING(
        questStyle = "RECORDING",
        journeyName = "이별 극복",
        journeyType = "FACE_EMOTION",
    ),
    ACTIVE(
        questStyle = "ACTIVE",
        journeyName = "감정 정리",
        journeyType = "PROCESS_EMOTION",
    ),
    ;

    companion object {
        fun fromQuestStyle(questName: String): QuestType =
            questName
                .trim()
                .let { questName ->
                    entries.firstOrNull {
                        it.questStyle.equals(
                            questName,
                            ignoreCase = true,
                        )
                    }
                } ?: RECORDING

        fun fromJourneyName(journeyName: String): QuestType =
            journeyName
                .trim()
                .let { journeyName -> entries.firstOrNull { it.journeyName == journeyName } }
                ?: RECORDING

        fun fromJourneyType(journeyType: String): QuestType =
            journeyType
                .trim()
                .let { journeyType -> entries.firstOrNull { it.journeyType == journeyType } }
                ?: RECORDING

        fun getJourneyName(journeyType: String): String = fromJourneyType(journeyType).journeyName
    }
}
