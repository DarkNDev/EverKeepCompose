package com.darkndev.everkeepcompose.ui.note.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.darkndev.everkeepcompose.ui.note.NoteViewModel
import com.darkndev.everkeepcompose.utils.getColor

@Composable
fun NoteColorButton(viewModel: NoteViewModel, reference: Int) {
    val colorInt = getColor(reference).second
    Box(
        modifier = Modifier
            .size(70.dp)
            .padding(8.dp)
            .shadow(16.dp, CircleShape)
            .clip(CircleShape)
            .background(colorInt)
            .clickable {
                viewModel.colorChanged(reference)
            }
    ) {
        Icon(
            modifier = Modifier.align(Alignment.Center),
            imageVector = Icons.Default.Check,
            contentDescription = "Selected",
            tint = if (viewModel.colorRef == reference) {
                getColor(reference).first
            } else Color.Transparent
        )
    }
}