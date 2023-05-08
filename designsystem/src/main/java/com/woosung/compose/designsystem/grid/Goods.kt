package com.woosung.compose.designsystem.grid

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.core_designsystem.R

@Composable
fun GoodGridContent(
    modifier: Modifier = Modifier,
    thumbnailURL: String,
    title: String,
    price: String,
    hasCoupon: Boolean = false,
    disCountPercent: String,
) {
    val context = LocalContext.current
    Column(modifier.padding(5.dp)) {
        Box() {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = thumbnailURL,
                placeholder = painterResource(
                    id = R.drawable.test,
                ),
                contentDescription = "",
                contentScale = ContentScale.Crop,
            )
            if (hasCoupon) {
                Surface(modifier = Modifier.align(Alignment.BottomStart), color = Color.Blue) {
                    Text(text = "쿠폰")
                }
            }
        }

        Column {
            Text(text = title)
            Row() {
                Text(text = price)
                Text(color = Color.Red, text = disCountPercent)
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
fun GoodPreview() {
    MaterialTheme() {
        GoodGridContent(title = "개인스보로", price = "13570", thumbnailURL = "", disCountPercent = "76")
    }
}
