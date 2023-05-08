package com.woosung.compose.designsystem.topbar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.core_designsystem.R

@Composable
fun GoodsHeader(
    modifier: Modifier = Modifier,
    title: String,
    iconUrl: String? = null,
    linkUrl: String? = null,
) {
    Surface(color = Color.White) {
        Row(
            modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = title, style = MaterialTheme.typography.headlineMedium, color = Color.Black)
            Spacer(modifier = modifier.width(5.dp))
            if (!iconUrl.isNullOrBlank()) {
                AsyncImage(
                    model = iconUrl,
                    placeholder = painterResource(id = R.drawable.ic_access_time_24),
                    contentDescription = "",
                )
            }
            Spacer(modifier = modifier.weight(1f))
            if (!linkUrl.isNullOrBlank()) {
                Text(
                    text = "전체",
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    GoodsHeader(title = "테스트", iconUrl = "g", linkUrl = "테스트")
}
