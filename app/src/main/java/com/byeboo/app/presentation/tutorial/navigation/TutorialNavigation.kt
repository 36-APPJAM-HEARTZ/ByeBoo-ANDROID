package com.byeboo.app.presentation.tutorial.navigation

import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.byeboo.app.core.navigation.Route
import com.byeboo.app.presentation.tutorial.TutorialRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToTutorial(navOptions: NavOptions? = null) {
    navigate(Tutorial, navOptions)
}

fun NavGraphBuilder.tutorialGraph(
    navigateToUp: () -> Unit,
    padding: Dp
) {
    composable<Tutorial> {
        TutorialRoute(
            navigateToUp = navigateToUp,
            bottomPadding = padding
        )
    }
}

@Serializable
data object Tutorial : Route
