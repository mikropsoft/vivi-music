package com.music.vivi.animation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun LimitedBorderAnimationView(
    isAnimated: Boolean = false,
    brush: Brush = Brush.sweepGradient(listOf(Color.Gray, Color.White)),
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    contentPadding: Dp = 0.dp,
    borderWidth: Dp = 1.dp,
    shape: Shape = RoundedCornerShape(12.dp),
    oneCircleDurationMillis: Int = 3000,
    interactionNumber: Int = 1,
    content: @Composable () -> Unit,
) {
    var shouldAnimate by rememberSaveable {
        mutableStateOf(false)
    }
    val scaleAnimationValue by animateFloatAsState(
        if (shouldAnimate) 1f else 0f,
        tween(800),
    )

    LaunchedEffect(true) {
        if (isAnimated) {
            shouldAnimate = true
            delay(interactionNumber * oneCircleDurationMillis.toLong())
            shouldAnimate = false
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "Infinite Color Animation")
    val degrees by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(durationMillis = oneCircleDurationMillis, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
        label = "Infinite Colors",
    )
    Surface(
        modifier =
            Modifier
                .clip(
                    shape,
                ).padding(borderWidth)
                .drawBehind {
                    if (isAnimated) {
                        scale(
                            scale = scaleAnimationValue,
                        ) {
                            rotate(degrees = degrees) {
                                drawCircle(
                                    brush = brush,
                                    radius = size.width,
                                    blendMode = BlendMode.SrcIn,
                                )
                            }
                        }
                    }
                },
        shape = shape,
    ) {
        Box(
            modifier =
                Modifier
                    .background(
                        color = backgroundColor,
                    ).padding(
                        contentPadding,
                    ),
            contentAlignment = Alignment.Center,
        ) {
            content()
        }
    }
}