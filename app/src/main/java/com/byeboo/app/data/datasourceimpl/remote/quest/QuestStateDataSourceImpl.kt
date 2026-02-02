package com.byeboo.app.data.datasourceimpl.remote.quest

import com.byeboo.app.data.datasource.remote.quest.QuestStateDataSource
import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.base.NullableBaseResponse
import com.byeboo.app.data.dto.response.quest.QuestCountResponseDto
import com.byeboo.app.data.dto.response.quest.QuestDialogueResponseDto
import com.byeboo.app.data.service.quest.QuestService
import javax.inject.Inject

class QuestStateDataSourceImpl
@Inject
constructor(
    private val questService: QuestService
) : QuestStateDataSource {
    override suspend fun updateQuestStartState(): NullableBaseResponse<Unit> = questService.updateQuestStartState()

    override suspend fun getQuestCount(): BaseResponse<QuestCountResponseDto> = questService.getQuestCount()

    override suspend fun getQuestDialogue(): BaseResponse<QuestDialogueResponseDto> = questService.getQuestDialogue()
}
