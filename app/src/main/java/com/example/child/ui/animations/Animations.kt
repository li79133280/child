package com.example.child.ui.animations

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale

// 页面切换动画
fun <S> pageTransition(): AnimatedContentTransitionScope<S>.() -> EnterTransition = {
    fadeIn(animationSpec = tween(300)) +
            slideInVertically(
                animationSpec = tween(300),
                initialOffsetY = { it / 20 }
            )
}

fun <S> pageExitTransition(): AnimatedContentTransitionScope<S>.() -> ExitTransition = {
    fadeOut(animationSpec = tween(200)) +
            slideOutVertically(
                animationSpec = tween(200),
                targetOffsetY = { -it / 20 }
            )
}

// 列表项入场动画
fun listItemEnter(index: Int): EnterTransition {
    val delay = (index * 50).coerceAtMost(300)
    return fadeIn(
        animationSpec = tween(
            durationMillis = 300,
            delayMillis = delay
        )
    ) + slideInVertically(
        animationSpec = tween(
            durationMillis = 300,
            delayMillis = delay
        ),
        initialOffsetY = { it / 10 }
    )
}

// 卡片点击缩放动画
@Composable
fun rememberCardScale(onClick: () -> Unit): Pair<Float, () -> Unit> {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "cardScale"
    )

    val triggerClick: () -> Unit = {
        isPressed = true
        onClick()
    }

    // 重置状态
    if (isPressed) {
        isPressed = false
    }

    return scale to triggerClick
}

// 弹性动画参数
object AnimSpecs {
    fun <T> springBouncy() = spring<T>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )

    fun <T> springSmooth() = spring<T>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMedium
    )

    fun <T> tweenFast() = tween<T>(durationMillis = 150)
    fun <T> tweenMedium() = tween<T>(durationMillis = 300)
    fun <T> tweenSlow() = tween<T>(durationMillis = 500)
}

// 渐入动画包装器
@Composable
fun FadeIn(
    modifier: Modifier = Modifier,
    delayMillis: Int = 0,
    content: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    androidx.compose.runtime.LaunchedEffect(Unit) {
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = 300,
                delayMillis = delayMillis
            )
        ) + slideInVertically(
            animationSpec = tween(
                durationMillis = 300,
                delayMillis = delayMillis
            ),
            initialOffsetY = { it / 10 }
        ),
        modifier = modifier
    ) {
        content()
    }
}

// 脉冲动画（用于提醒等）
@Composable
fun rememberPulse(): Float {
    var active by remember { mutableStateOf(true) }
    val alpha by animateFloatAsState(
        targetValue = if (active) 1f else 0.6f,
        animationSpec = tween(800),
        label = "pulse"
    )

    androidx.compose.runtime.LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(800)
            active = !active
        }
    }

    return alpha
}
