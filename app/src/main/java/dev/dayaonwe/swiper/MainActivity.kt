package dev.dayaonwe.swiper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.dayaonwe.swiper.ui.theme.SwiperTheme
import dev.dayaonweb.swiper.AnchoredDragDefaults
import dev.dayaonweb.swiper.BasicSwiper
import dev.dayaonweb.swiper.ContentSwiper
import dev.dayaonweb.swiper.RawSwiper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SwiperTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        BasicSwiperUsage(modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}


@Composable
fun BasicSwiperUsage(modifier: Modifier = Modifier) {
    BasicSwiper(
        modifier = modifier,
        displayText = "Basic Swiper",
        containerColor = Color.Green,
        progressContent = { LinearProgressIndicator() },
        onSwipeComplete = {

        })
}

@Composable
fun ContentSwiperUsage(modifier: Modifier = Modifier) {
    ContentSwiper(
        modifier = modifier,
        containerColor = Color.Blue,
        postSwipeContent = {
            CircularProgressIndicator(color = Color.White)
        },
        preSwipeContent = {
            Text(
                text = "Swipe Me",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
        },
        onSwipeComplete = {})
}

@Composable
fun OutlinedVariantContentSwiperUsage(modifier: Modifier = Modifier) {
    ContentSwiper(
        contentPadding = PaddingValues(),
        modifier = modifier
            .border(
                1.dp, Color.Black,
                RoundedCornerShape(32.dp)
            ),
        containerColor = Color.White,
        containerShape = MaterialTheme.shapes.large,
        postSwipeContent = {
            Text(text = "Content Swiper", color = Color.Black, modifier = Modifier.padding(20.dp))
        },
        preSwipeContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(4.dp)
                        .background(Color.Black, RoundedCornerShape(40.dp))
                        .padding(16.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Swipe Me",
                    color = Color.Black,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp
                )
            }
        },
        onSwipeComplete = {})
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RawSwiperUsage(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val color by infiniteTransition.animateColor(
        initialValue = Color.Green,
        targetValue = Color.Red, // Dark Red
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    RawSwiper(
        modifier = modifier
            .drawBehind {
                drawRoundRect(
                    color = color,
                    cornerRadius = CornerRadius(40.dp.toPx(), 40.dp.toPx()),
                    style = Stroke(width = 2.dp.toPx())
                )
            },
        containerColor = Color.Transparent,
        postSwipeContent = {
            Text(
                text = "Swipe Done", color = color, fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
            )
        },
        preSwipeContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = null,
                    tint = color
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Swipe",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = color
                )
            }
        },
        onSwipeComplete = {},
        anchoredDragState = AnchoredDragDefaults.rememberSwiperDragState(width = 350.dp),
        width = 350.dp
    )
}


@Preview
@Composable
private fun SwiperPreview() {
    SwiperTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                verticalArrangement = Arrangement.Absolute.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                BasicSwiperUsage()
                ContentSwiperUsage()
                RawSwiperUsage()
                OutlinedVariantContentSwiperUsage()
            }
        }
    }
}