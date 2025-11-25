package com.byeboo.app.presentation.main

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )

        viewModel.handleIntent(intent)

        setContent {

            val notificationQuestId by viewModel.notificationQuestId.collectAsStateWithLifecycle()
            val isLoginCompleted by viewModel.isLoginCompleted.collectAsStateWithLifecycle()

            ByeBooTheme {
                val context = LocalContext.current

                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { isGranted ->
                        if (isGranted) {
                            viewModel.hasNotificationPermission()
                        }
                    }
                )

                LaunchedEffect(isLoginCompleted) {
                    if (isLoginCompleted) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            val hasPermission = ContextCompat.checkSelfPermission(
                                context,
                                POST_NOTIFICATIONS
                            ) == PackageManager.PERMISSION_GRANTED

                            if (!hasPermission) {
                                permissionLauncher.launch(POST_NOTIFICATIONS)
                            }
                        }
                    }
                }

                MainScreen(
                    notificationQuestId = notificationQuestId,
                    onClearQuestId = { viewModel.clearNotificationQuestId() }
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        viewModel.handleIntent(intent)
    }
}
