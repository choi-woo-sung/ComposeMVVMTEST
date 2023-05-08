package com.woosung.compose.designsystem.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun FooterButton(
    modifier: Modifier = Modifier.fillMaxWidth(),
    onClick: () -> Unit = {},
    text: String,
    iconUrl: String? = null,
) {
    Button(
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black,
        ),
        border = BorderStroke(1.dp, Color.Gray),
        onClick = onClick,
    ) {
        if (!iconUrl.isNullOrEmpty()) {
            AsyncImage(model = iconUrl, contentDescription = text)
        }
        Text(text = text)
    }
}

@Preview(showBackground = true)
@Composable
fun FooterButtonPreview() {
    FooterButton(
        text = "테스트",
    )
}
