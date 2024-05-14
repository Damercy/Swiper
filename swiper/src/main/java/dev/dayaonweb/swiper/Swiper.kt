package dev.dayaonweb.swiper

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
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

/**
 * Basic Swiper for a simple swiping button. Recommended to use when looking for a simple swipe button with basic customization applied.
 * @param displayText The text to display. Pass empty string to hide the text.
 * @param width The width of the button. It is recommended to pass width as per requirement. Defaults to [AnchoredDragDefaults.defaultWidth].
 * @param containerColor Background color of the button. Pass [Color.Transparent] & use [Modifier.background] for custom button backgrounds like outlined background etc.
 * @param showProgress If true, shows progress indicator even before swipe is complete. Else shows progress indicator after swipe completion. Defaults to false.
 * @param containerShape Shape of the button. Defaults to [MaterialTheme.shapes].
 * @param displayTextStyle [TextStyle] for [displayText]. Defaults to [MaterialTheme.typography.titleMedium of color [Color.White], 18sp size & font weight [FontWeight.W600]].
 * @param contentPadding [PaddingValues] to use for padding contents of the button. Alternatively, pass empty [PaddingValues] & use [Modifier.padding] for custom padding.
 * @param swipeIcon [ImageVector] to use for the swipe icon. Defaults to [Icons.AutoMirrored.Default].
 * @param showIcon If true, shows the swipe icon before swipe is complete. Defaults to true.
 * @param progressContent [Composable] content slot to show content once swipe is complete.
 * @param onSwipeComplete [Function] callback to call when swipe is complete.
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)
@Composable
fun BasicSwiper(
    modifier: Modifier = Modifier,
    displayText: String,
    width: Dp = AnchoredDragDefaults.defaultWidth,
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
    val anchoredDragState = AnchoredDragDefaults.rememberSwiperDragState(width = width)
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


/**
 * Content Swiper for a customizable swiping button. Recommended to use when looking to customize swipe functionalities like icon, pre/post swipe text, animation etc.
 * @param width The width of the button. It is recommended to pass width as per requirement. Defaults to [AnchoredDragDefaults.defaultWidth].
 * @param containerColor Background color of the button. Pass [Color.Transparent] & use [Modifier.background] for custom button backgrounds like outlined background etc.
 * @param containerShape Shape of the button. Defaults to [MaterialTheme.shapes].
 * @param contentPadding [PaddingValues] to use for padding contents of the button. Alternatively, pass empty [PaddingValues] & use [Modifier.padding] for custom padding.
 * @param onSwipeComplete [Function] callback to call when swipe is complete.
 * @param preSwipeContent [Composable] content slot to show content before swipe.
 * @param postSwipeContent [Composable] content slot to show content after swipe.
 * @param velocityThresholdMultiplier Multiplier for velocity threshold. Defaults to [AnchoredDragDefaults.MULTIPLIER_VELOCITY].
 * @param positionalThresholdMultiplier Multiplier for positional threshold. Defaults to [AnchoredDragDefaults.MULTIPLIER_POSITION].
 * @param swipeShowProgressThreshold Threshold for showing progress. Setting it to 0.5f means swipe will be completed after 50% of the swipe is done. Defaults to [AnchoredDragDefaults.THRESHOLD_AFTER_SWIPE_PROGRESS].
 * @param dragAnimationSpec Animation spec for dragging. Defaults to [spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)].
 * @param preToPostTransitionAnimationSpec Animation spec for pre-to-post transition. This spec is used to transition content change from pre-swipe to post-swipe.
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)
@Composable
fun ContentSwiper(
    modifier: Modifier = Modifier,
    containerColor: Color,
    width: Dp = AnchoredDragDefaults.defaultWidth,
    containerShape: Shape = MaterialTheme.shapes.medium,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    velocityThresholdMultiplier: Float = AnchoredDragDefaults.MULTIPLIER_VELOCITY,
    positionalThresholdMultiplier: Float = AnchoredDragDefaults.MULTIPLIER_POSITION,
    swipeShowProgressThreshold: Float = AnchoredDragDefaults.THRESHOLD_AFTER_SWIPE_PROGRESS,
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

    val anchoredDragState = AnchoredDragDefaults.rememberSwiperDragState(
        width = width,
        positionThreshold = positionalThresholdMultiplier,
        velocityThreshold = velocityThresholdMultiplier,
        dragAnimationSpec = dragAnimationSpec
    )
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

/**
 * Raw Swiper for a fully customizable swiping button. Recommended to use when looking to customize specifics like drag points,[AnchoredDraggableState] etc.
 * @param width The width of the button. It is recommended to pass width as per requirement. Defaults to 350.dp.
 * @param containerColor Background color of the button. Pass [Color.Transparent] & use [Modifier.background] for custom button backgrounds like outlined background etc.
 * @param containerShape Shape of the button. Defaults to [MaterialTheme.shapes].
 * @param contentPadding [PaddingValues] to use for padding contents of the button. Alternatively, pass empty [PaddingValues] & use [Modifier.padding] for custom padding.
 * @param onSwipeComplete [Function] callback to call when swipe is complete.
 * @param preSwipeContent [Composable] content slot to show content before swipe.
 * @param postSwipeContent [Composable] content slot to show content after swipe.
 * @param anchoredDragState [AnchoredDraggableState] to use for swiping.
 * @param swipeShowProgressThreshold Threshold for showing progress. Setting it to 0.5f means swipe will be completed after 50% of the swipe is done. Defaults to [AnchoredDragDefaults.THRESHOLD_AFTER_SWIPE_PROGRESS].
 */
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