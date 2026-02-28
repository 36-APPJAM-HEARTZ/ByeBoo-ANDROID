package com.byeboo.app.presentation.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.navOptions
import com.byeboo.app.core.designsystem.component.backhandler.ByeBooBackHandler
import com.byeboo.app.core.designsystem.component.snackbar.CustomSnackBar
import com.byeboo.app.core.designsystem.component.snackbar.CustomSnackBarVisuals
import com.byeboo.app.core.designsystem.event.LocalSnackBarTrigger
import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.domain.model.JourneyStatusType
import com.byeboo.app.presentation.home.navigation.Home
import com.byeboo.app.presentation.main.component.MainBottomBar
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    navigator: MainNavigator = rememberMainNavigator(),
    viewModel: MainViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    var isNavigating by remember { mutableStateOf(false) }
    val currentTab = navigator.currentTab
    val showBottomBar = navigator.showBottomBar()
    val status by viewModel.journeyStatus.collectAsStateWithLifecycle()
    val isMoveToQuestHome by viewModel.questHomeNavigation.collectAsStateWithLifecycle()

    val onShowSnackBar: (CustomSnackBarType) -> Unit = { type ->
        scope.launch {
            snackBarHostState.currentSnackbarData?.dismiss()
            val job =
                launch {
                    snackBarHostState.showSnackbar(
                        CustomSnackBarVisuals(
                            type = type
                        ),
                    )
                }
            delay(3000L)
            job.cancel()
        }
    }

    val snackBarBottomInset =
        if (showBottomBar) {
            screenHeightDp(8.dp)
        } else {
            screenHeightDp(68.dp)
        }

    val navOptions =
        navOptions {
            popUpTo(Home) {
                saveState = true
                inclusive = false
            }
            launchSingleTop = true
            restoreState = true
        }

    val moveToQuestNavigation: () -> Unit = {
        scope.launch {
            isNavigating = true
            try {
                val journeyStatus = status ?: JourneyStatusType.BEFORE_START

                when (journeyStatus) {
                    JourneyStatusType.BEFORE_START, JourneyStatusType.UNKNOWN -> {
                        viewModel.trackJourneyStart()
                        navigator.navigateToQuestStart(null, navOptions)
                    }

                    JourneyStatusType.COMPLETED -> {
                        navigator.navigateToOffboardingCompletedGuide(navOptions)
                    }

                    JourneyStatusType.IN_PROGRESS -> {
                        navigator.navigateToQuest(navOptions)
                    }
                }
            } finally {
                isNavigating = false
            }
        }
    }

    if (showBottomBar) {
        if (currentTab == MainNavTab.HOME) {
            ByeBooBackHandler("뒤로가기를 한 번 더 누르면 앱이 종료됩니다")
        } else {
            BackHandler { navigator.navigate(MainNavTab.HOME) }
        }
    }

    LaunchedEffect(isMoveToQuestHome, status) {
        if (isMoveToQuestHome && !isNavigating && status != JourneyStatusType.UNKNOWN) {
            delay(100L)
            moveToQuestNavigation()
            delay(50L)
            viewModel.clearNavigateQuestHome()
        }
    }

    CompositionLocalProvider(
        LocalSnackBarTrigger provides onShowSnackBar,
    ) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(
                    hostState = snackBarHostState,
                    modifier =
                        Modifier
                            .padding(horizontal = screenWidthDp(24.dp))
                            .padding(bottom = snackBarBottomInset),
                ) { snackBar ->
                    val customVisuals = snackBar.visuals as? CustomSnackBarVisuals
                    val type = customVisuals?.type ?: CustomSnackBarType.ALERT
                    CustomSnackBar(customSnackBarType = type)
                }
            },
            bottomBar = {
                MainBottomBar(
                    visible = navigator.showBottomBar(),
                    tabs = MainNavTab.entries.toImmutableList(),
                    currentTab = currentTab,
                    onTabSelected = { selectedTab ->
                        if (isNavigating || selectedTab == currentTab) return@MainBottomBar

                        if (selectedTab == MainNavTab.QUEST) {
                            moveToQuestNavigation()
                        } else {
                            scope.launch {
                                isNavigating = true
                                try {
                                    navigator.navigate(selectedTab)
                                } finally {
                                    isNavigating = false
                                }
                            }
                        }
                    },
                )
            },
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(ByeBooTheme.colors.background),
        ) { paddingValues ->
            MainNavHost(
                navigator = navigator,
                paddingValues = paddingValues,
                modifier = Modifier,
            )
        }
    }
}
