package com.byeboo.app.core.util

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.byeboo.app.core.navigation.Route

inline fun <reified T : Route, reified S : Route> NavGraphBuilder.routeNavigation(noinline builder: NavGraphBuilder.() -> Unit) {
    navigation(
        route = T::class.simpleName ?: error("Route type must have simpleName"),
        startDestination = S::class.simpleName ?: error("StartDestination must have simpleName"),
        builder = builder,
    )
}
