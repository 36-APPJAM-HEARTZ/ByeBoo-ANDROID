package com.byeboo.app.domain.repository

interface NewJourneyRepository {
    suspend fun postNewJourney(journey: String): Result <Unit>
}
