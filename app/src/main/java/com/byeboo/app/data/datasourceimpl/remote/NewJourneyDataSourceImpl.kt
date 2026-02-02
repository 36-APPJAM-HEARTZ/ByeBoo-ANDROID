package com.byeboo.app.data.datasourceimpl.remote

import com.byeboo.app.data.datasource.remote.NewJourneyDataSource
import com.byeboo.app.data.dto.base.NullableBaseResponse
import com.byeboo.app.data.service.NewJourneyService
import javax.inject.Inject

class NewJourneyDataSourceImpl
    @Inject
    constructor(
        private val newJourneyService: NewJourneyService,
    ) : NewJourneyDataSource {
        override suspend fun postNewJourney(journey: String): NullableBaseResponse<Unit> =
            newJourneyService.postNewJourney(
                journey,
            )
    }
