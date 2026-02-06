package com.byeboo.app.domain.usecase

import com.byeboo.app.domain.model.notification.FcmTokenModel
import com.byeboo.app.domain.repository.fcm.FcmTokenRepository
import javax.inject.Inject

class UpdateFcmTokenUseCase
    @Inject
    constructor(
        private val fcmTokenRepository: FcmTokenRepository,
    ) {
        suspend operator fun invoke(token: String) {
            fcmTokenRepository.updateFcmToken(FcmTokenModel(token))
        }
    }
