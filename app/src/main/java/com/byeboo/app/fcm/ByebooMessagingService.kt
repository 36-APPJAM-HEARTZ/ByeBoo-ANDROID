package com.byeboo.app.fcm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.byeboo.app.R
import com.byeboo.app.domain.usecase.UpdateFcmTokenUseCase
import com.byeboo.app.presentation.main.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ByebooMessagingService : FirebaseMessagingService() {
    @Inject
    lateinit var updateFcmTokenUseCase: UpdateFcmTokenUseCase

    private val fcmServiceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        fcmServiceScope.launch {
            runCatching {
                updateFcmTokenUseCase(token)
            }.onFailure { e ->
                Timber.e(e, "FCM 토큰 갱신 실패: $token")
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.notification?.title
        val body = message.notification?.body
        val questId = message.data["questId"] ?: "1"

        message.notification?.let {
            showNotification(title, body, questId)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fcmServiceScope.cancel()
    }

    private fun showNotification(
        title: String?,
        message: String?,
        questId: String
    ) {
        val notifyId = System.currentTimeMillis().toInt()

        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("questId", questId)
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            notifyId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notifyId, builder.build())
    }


    companion object {
        const val CHANNEL_ID = "BYEBOO"
        const val CHANNEL_NAME = "Byeboo 알림 채널"
    }
}