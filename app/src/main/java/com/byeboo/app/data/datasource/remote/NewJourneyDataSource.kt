package com.byeboo.app.data.datasource.remote

import com.byeboo.app.data.dto.base.NullableBaseResponse

interface NewJourneyDataSource {
    suspend fun postNewJourney(journey: String): NullableBaseResponse<Unit>
}
