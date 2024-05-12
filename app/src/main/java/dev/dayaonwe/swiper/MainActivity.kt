package dev.dayaonwe.swiper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.dayaonwe.swiper.ui.theme.SwiperTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SwiperTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ListComposable(
                        n = 100,
                        modifier = Modifier
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Composable
fun ListComposable(modifier: Modifier = Modifier, n: Int) {
    val listComp = remember { mutableStateListOf<String>() }
    LaunchedEffect(key1 = Unit) {
        for (i in 0 until n) {
            listComp.add(Random.nextInt(100, 1001).toString())
        }
    }
    LazyColumn(modifier = modifier) {
        items(listComp.size) {
            val item = listComp[it]
            Text(text = "Item $item", style = MaterialTheme.typography.bodyMedium)
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ListComposablePreview() {
    SwiperTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Box(contentAlignment = Alignment.Center) {
                ListComposable(n = 100)
            }
        }
    }
}