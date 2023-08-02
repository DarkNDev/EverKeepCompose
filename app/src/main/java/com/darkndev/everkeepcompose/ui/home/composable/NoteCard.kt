package com.darkndev.everkeepcompose.ui.home.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.darkndev.everkeepcompose.models.Note
import com.darkndev.everkeepcompose.ui.theme.NoteCardTheme
import com.darkndev.everkeepcompose.utils.getColor

@Composable
fun NoteCard(
    note: Note,
    modifier: Modifier,
    navigate: () -> Unit,
    delete: () -> Unit
) {
    val colors = getColor(note.color)
    NoteCardTheme(colors = colors) {
        ElevatedCard(
            modifier = modifier,
            onClick = navigate,
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(12.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                maxLines = 8,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = note.toTime(),
                    style = MaterialTheme.typography.bodyMedium
                )
                IconButton(onClick = delete) {
                    Icon(Icons.Default.Delete, contentDescription = "Localized description")
                }
            }
        }
    }
}