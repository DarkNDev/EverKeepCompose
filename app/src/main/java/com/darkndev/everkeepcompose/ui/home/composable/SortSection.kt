package com.darkndev.everkeepcompose.ui.home.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.darkndev.everkeepcompose.utils.SortOrder

@Composable
fun SortSection(
    sortOrder: SortOrder = SortOrder.PRIORITY,
    onOrderChange: (SortOrder) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        SortButton(
            text = "Title",
            selected = sortOrder == SortOrder.TITLE,
            onSelected = { onOrderChange(SortOrder.TITLE) })
        SortButton(
            text = "Priority",
            selected = sortOrder == SortOrder.PRIORITY,
            onSelected = { onOrderChange(SortOrder.PRIORITY) })
        SortButton(
            text = "Time",
            selected = sortOrder == SortOrder.TIME,
            onSelected = { onOrderChange(SortOrder.TIME) })
    }
}