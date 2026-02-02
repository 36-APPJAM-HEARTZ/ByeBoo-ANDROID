package com.byeboo.app.presentation.tutorial.navigation

import androidx.compose.foundation.layout.PaddingValues
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
    paddingValues: PaddingValues,
) {
    composable<Tutorial> {
        TutorialRoute(
            navigateToUp = navigateToUp,
            paddingValues = paddingValues,
        )
    }
}

@Serializable
data object Tutorial : Route
