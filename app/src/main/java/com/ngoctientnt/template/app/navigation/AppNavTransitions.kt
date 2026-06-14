package com.ngoctientnt.template.app.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith

object AppNavTransitions {

    private const val DurationMillis = 320

    private val easing = FastOutSlowInEasing

    val push: AnimatedContentTransitionScope<*>.() -> ContentTransform = {
        slideInHorizontally(
            initialOffsetX = { fullWidth -> fullWidth },
            animationSpec = tween(DurationMillis, easing = easing),
        ) + fadeIn(animationSpec = tween(DurationMillis, easing = easing)) togetherWith
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth / 4 },
                animationSpec = tween(DurationMillis, easing = easing),
            ) + fadeOut(animationSpec = tween(DurationMillis, easing = easing))
    }

    val pop: AnimatedContentTransitionScope<*>.() -> ContentTransform = {
        slideInHorizontally(
            initialOffsetX = { fullWidth -> -fullWidth / 4 },
            animationSpec = tween(DurationMillis, easing = easing),
        ) + fadeIn(animationSpec = tween(DurationMillis, easing = easing)) togetherWith
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(DurationMillis, easing = easing),
            ) + fadeOut(animationSpec = tween(DurationMillis, easing = easing))
    }

    val predictivePop: AnimatedContentTransitionScope<*>.(Int) -> ContentTransform = { _ ->
        pop()
    }
}
