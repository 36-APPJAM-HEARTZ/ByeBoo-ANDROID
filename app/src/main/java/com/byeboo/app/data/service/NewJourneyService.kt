package com.byeboo.app.data.service

import com.byeboo.app.data.dto.base.NullableBaseResponse
import retrofit2.http.POST
import retrofit2.http.Query

interface NewJourneyService {
    @POST("/api/v1/quests/journey")
    suspend fun postNewJourney(
        @Query("journey") journey: String
    ): NullableBaseResponse<Unit>
}
