package com.darkndev.everkeepcompose.ui.note.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun SettingItem(
    imageVector: ImageVector,
    color: Color = Color.Black,
    contentDescription: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier
                .padding(top = 12.dp, start = 12.dp, end = 12.dp)
                .size(26.dp),
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = color
        )
        Text(
            modifier = Modifier.padding(12.dp),
            text = contentDescription,
            color = color
        )
    }
}