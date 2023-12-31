package com.darkndev.everkeepcompose.ui.home.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LabelCard(label: String, selectedLabel: String, onClick: (label: String) -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (selectedLabel == label) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background)
            .border(
                width = 1.dp,
                color = if (selectedLabel == label) Color.Transparent else MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                onClick(label)
            }
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(12.dp),
            color = if (selectedLabel == label) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primary
        )
    }
}