package dev.dayaonweb.swiper

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)
@Composable
fun BasicSwiper(
    modifier: Modifier = Modifier,
    displayText: String,
    width: Dp = 350.dp,
    containerColor: Color,
    containerShape: Shape = MaterialTheme.shapes.medium,
    showProgress: Boolean = false,
    displayTextStyle: TextStyle = MaterialTheme.typography.titleMedium.copy(
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.W600
    ),
    contentPadding: PaddingValues = PaddingValues(16.dp),
    swipeIcon: ImageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
    showIcon: Boolean = true,
    progressContent: @Composable () -> Unit,
    onSwipeComplete: () -> Unit,
) {
    val anchoredDragState = AnchoredDragDefaults.rememberSwiperDragState()
    var showProgressContent by rememberSaveable(showProgress) {
        mutableStateOf(showProgress)
    }

    LaunchedEffect(key1 = anchoredDragState.requireOffset()) {
        if (anchoredDragState.progress != 1.0f && anchoredDragState.progress >= 0.65f && !showProgressContent) {
            showProgressContent = true
            onSwipeComplete()
        }
    }

    Box(
        modifier = modifier
            .width(width)
            .background(color = containerColor, shape = containerShape)
            .anchoredDraggable(
                state = anchoredDragState,
                orientation = Orientation.Horizontal,
                enabled = !showProgressContent,
            )
            .padding(contentPadding)
            .animateContentSize(),
    ) {
        Crossfade(targetState = showProgressContent, label = "") { showProgressContent ->
            when (showProgressContent) {
                true -> {
                    Box(
                        modifier = Modifier
                            .width(350.dp)
                            .height(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        progressContent()
                    }
                }

                false -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (showIcon) {
                            Icon(
                                imageVector = swipeIcon,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier
                                    .size(32.dp)
                                    .offset {
                                        IntOffset(
                                            y = 0,
                                            x = anchoredDragState
                                                .requireOffset()
                                                .roundToInt()
                                        )
                                    },
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text(
                            text = displayText,
                            style = displayTextStyle,
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset {
                                    IntOffset(
                                        y = 0,
                                        x = anchoredDragState
                                            .requireOffset()
                                            .roundToInt()
                                    )
                                }
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)
@Composable
fun ContentSwiper(
    modifier: Modifier = Modifier,
    containerColor: Color,
    width: Dp = 350.dp,
    containerShape: Shape = MaterialTheme.shapes.medium,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    velocityThresholdMultiplier: Float = 2f,
    positionalThresholdMultiplier: Float = 0.5f,
    swipeShowProgressThreshold: Float = 0.65f,
    dragAnimationSpec: AnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    ),
    preToPostTransitionAnimationSpec: AnimatedContentTransitionScope<Boolean>.() -> ContentTransform = {
        (fadeIn(animationSpec = tween(220, delayMillis = 90)) +
                scaleIn(initialScale = 0.92f, animationSpec = tween(220, delayMillis = 90)))
            .togetherWith(fadeOut(animationSpec = tween(90)))
    },
    postSwipeContent: @Composable () -> Unit,
    preSwipeContent: @Composable BoxScope.() -> Unit,
    onSwipeComplete: () -> Unit,
) {

    val maxPx = with(LocalDensity.current) { width.toPx() }
    val anchoredDragState = remember {
        AnchoredDraggableState(
            initialValue = 0f,
            positionalThreshold = { position: Float -> position * positionalThresholdMultiplier },
            velocityThreshold = { maxPx * velocityThresholdMultiplier },
            anchors = DraggableAnchors {
                0f at 0f
                maxPx at maxPx
            },
            animationSpec = dragAnimationSpec
        )
    }
    var showProgressContent by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = anchoredDragState.requireOffset()) {
        if (anchoredDragState.progress != 1.0f && anchoredDragState.progress >= swipeShowProgressThreshold && !showProgressContent) {
            showProgressContent = true
            onSwipeComplete()
        }
    }

    Box(
        modifier = modifier
            .width(width)
            .background(color = containerColor, shape = containerShape)
            .anchoredDraggable(
                state = anchoredDragState,
                orientation = Orientation.Horizontal,
                enabled = !showProgressContent,
            )
            .padding(contentPadding),
    ) {
        AnimatedContent(
            targetState = showProgressContent,
            label = "",
            transitionSpec = preToPostTransitionAnimationSpec
        ) { showProgressContent ->
            when (showProgressContent) {
                true -> postSwipeContent()
                false -> {
                    Box(modifier = Modifier
                        .offset {
                            IntOffset(
                                y = 0,
                                x = anchoredDragState
                                    .requireOffset()
                                    .roundToInt()
                            )
                        }) {
                        preSwipeContent()
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RawSwiper(
    modifier: Modifier = Modifier,
    containerColor: Color,
    width: Dp,
    containerShape: Shape = MaterialTheme.shapes.medium,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    anchoredDragState: AnchoredDraggableState<Float>,
    swipeShowProgressThreshold: Float = AnchoredDragDefaults.THRESHOLD_AFTER_SWIPE_PROGRESS,
    preToPostTransitionSpec: AnimatedContentTransitionScope<Boolean>.() -> ContentTransform = {
        (fadeIn(animationSpec = tween(220, delayMillis = 90)) +
                scaleIn(initialScale = 0.92f, animationSpec = tween(220, delayMillis = 90)))
            .togetherWith(fadeOut(animationSpec = tween(90)))
    },
    postSwipeContent: @Composable () -> Unit,
    preSwipeContent: @Composable BoxScope.() -> Unit,
    onSwipeComplete: () -> Unit,
) {
    var showProgressContent by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = anchoredDragState.requireOffset()) {
        if (anchoredDragState.progress != 1.0f && anchoredDragState.progress >= swipeShowProgressThreshold && !showProgressContent) {
            showProgressContent = true
            onSwipeComplete()
        }
    }

    Box(
        modifier = modifier
            .width(width)
            .background(color = containerColor, shape = containerShape)
            .anchoredDraggable(
                state = anchoredDragState,
                orientation = Orientation.Horizontal,
                enabled = !showProgressContent,
            )
            .padding(contentPadding),
    ) {
        AnimatedContent(
            targetState = showProgressContent,
            label = "",
            transitionSpec = preToPostTransitionSpec
        ) { showProgressContent ->
            when (showProgressContent) {
                true -> postSwipeContent()
                false -> {
                    Box(modifier = Modifier
                        .offset {
                            IntOffset(
                                y = 0,
                                x = anchoredDragState
                                    .requireOffset()
                                    .roundToInt()
                            )
                        }) {
                        preSwipeContent()
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun SwiperPreview() {
    MaterialTheme {
        Surface {
            Column {
                BasicSwiper(
                    displayText = "Basic swiper",
                    containerColor = Color.Black,
                    progressContent = { CircularProgressIndicator() },
                    onSwipeComplete = {})
                Spacer(modifier = Modifier.height(32.dp))
                Spacer(modifier = Modifier.height(32.dp))
                ContentSwiper(
                    containerColor = Color.Red,
                    postSwipeContent = { Text(text = "Swipe complete!") },
                    preSwipeContent = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.Send,
                                contentDescription = null
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.Send,
                                contentDescription = null
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.Send,
                                contentDescription = null
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.Send,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Custom content swiper",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.surface
                            )
                        }
                    },
                    onSwipeComplete = {

                    })
                Spacer(modifier = Modifier.height(32.dp))
                RawSwiper(
                    containerColor = Color.Yellow,
                    width = 100.dp,
                    postSwipeContent = { Text(text = "Raw swiper done") },
                    preSwipeContent = { Text(text = "Raw swiper") },
                    onSwipeComplete = {},
                    anchoredDragState = AnchoredDraggableState(
                        initialValue = 0f,
                        positionalThreshold = { position: Float -> position * 0.5f },
                        velocityThreshold = { 100f * 0.5f },
                        anchors = DraggableAnchors {
                            0f at 0f
                            100f at 100f
                        },
                        animationSpec = spring()
                    )
                )
            }
        }
    }
}