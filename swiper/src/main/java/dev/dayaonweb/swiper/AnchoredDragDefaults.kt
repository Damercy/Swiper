package dev.dayaonweb.swiper

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object AnchoredDragDefaults {

    /**
     * Default velocity multiplier of swiper.
     * 2f (i.e. Swipe velocity should be twice the drag velocity)
     */
    const val MULTIPLIER_VELOCITY = 2f

    /**
     * Default positional multiplier of swiper.
     * 0.5f (i.e. 50%+ of swipe switches to the next draggable point)
     */
    const val MULTIPLIER_POSITION = 0.5f

    /**
     * Default threshold after swipe of swiper.
     * 0.65f (i.e. 65%+ of swipe triggers onSwipeComplete callback.)
     */
    const val THRESHOLD_AFTER_SWIPE_PROGRESS = 0.65f

    /**
     * Default width of swiper.
     * 350 dp.
     */
    val defaultWidth = 350.dp


    @OptIn(ExperimentalFoundationApi::class)
    internal fun default(
        widthPx: Float,
        positionThreshold: Float,
        velocityThreshold: Float,
        spec: AnimationSpec<Float>,
    ): AnchoredDraggableState<Float> {
        return AnchoredDraggableState(
            initialValue = 0f,
            positionalThreshold = { position: Float -> position * positionThreshold },
            velocityThreshold = { widthPx * velocityThreshold },
            anchors = DraggableAnchors {
                0f at 0f
                widthPx at widthPx
            },
            animationSpec = spec
        )
    }


    /**
     * Default [AnchoredDraggableState] used by Swiper.
     * @param width The width of the swipe button in dp. Defaults to [defaultWidth].
     * @param positionThreshold The threshold after swipe of swiper. Defaults to [THRESHOLD_AFTER_SWIPE_PROGRESS].
     * @param velocityThreshold The velocity threshold of swiper. Defaults to [MULTIPLIER_VELOCITY].
     * @param dragAnimationSpec The animation spec of swiper. Defaults to [spring].
     */
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun rememberSwiperDragState(
        width: Dp = defaultWidth,
        positionThreshold: Float = THRESHOLD_AFTER_SWIPE_PROGRESS,
        velocityThreshold: Float = MULTIPLIER_VELOCITY,
        dragAnimationSpec: AnimationSpec<Float> = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
    ): AnchoredDraggableState<Float> {
        val widthPx = with(LocalDensity.current) { width.toPx() }
        return remember(widthPx) {
            default(
                widthPx,
                positionThreshold,
                velocityThreshold,
                dragAnimationSpec
            )
        }
    }


}