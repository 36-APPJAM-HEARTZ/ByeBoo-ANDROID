package com.byeboo.app.data.repositoryimpl

import com.byeboo.app.data.datasource.remote.NewJourneyDataSource
import com.byeboo.app.domain.repository.NewJourneyRepository
import javax.inject.Inject

class NewJourneyRepositoryImpl
    @Inject
    constructor(
        private val newJourneyDataSource: NewJourneyDataSource,
    ) : NewJourneyRepository {
        override suspend fun postNewJourney(journey: String): Result<Unit> =
            runCatching {
                val response = newJourneyDataSource.postNewJourney(journey)
                if (response.success) {
                    Unit
                } else {
                    throw IllegalStateException(response.message)
                }
            }
    }
