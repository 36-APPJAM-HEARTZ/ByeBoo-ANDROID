package com.byeboo.app.data.mapper.auth

import com.byeboo.app.data.dto.request.auth.UserInfoRequestDto
import com.byeboo.app.data.dto.response.auth.UserJourneyResponseDto
import com.byeboo.app.domain.model.auth.UserInfoModel
import com.byeboo.app.domain.model.auth.UserJourney

fun UserInfoModel.toData(): UserInfoRequestDto =
    UserInfoRequestDto(
        name = this.name,
        feeling = this.feeling,
        questStyle = this.questStyle,
    )

fun UserJourneyResponseDto.toDomain(): UserJourney =
    UserJourney(
        journey = this.journey,
        description = this.description,
    )
