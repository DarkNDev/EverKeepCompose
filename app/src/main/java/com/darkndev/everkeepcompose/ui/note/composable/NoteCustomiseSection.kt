package com.darkndev.everkeepcompose.ui.note.composable

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.darkndev.everkeepcompose.models.Note
import com.darkndev.everkeepcompose.ui.home.composable.LabelCard
import com.darkndev.everkeepcompose.ui.note.NoteViewModel
import com.darkndev.everkeepcompose.ui.theme.NoteComposeTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NoteCustomiseSection(
    viewModel: NoteViewModel,
    onDismissRequest: () -> Unit
) {
    val scrollColorState = rememberScrollState()
    val sheetState = rememberModalBottomSheetState()
    val labels by viewModel.allLabels.collectAsStateWithLifecycle()
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        windowInsets = WindowInsets(0, 0, 0, 0)
    ) {
        Text(
            modifier = Modifier.padding(start = 12.dp, end = 12.dp),
            text = "Color",
            style = MaterialTheme.typography.bodyLarge
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp)
                .horizontalScroll(scrollColorState),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            NoteColorButton(
                viewModel = viewModel,
                reference = -1
            )
            Note.colorReference.forEach { reference ->
                NoteColorButton(
                    viewModel = viewModel,
                    reference = reference
                )
            }
        }
        Text(
            modifier = Modifier.padding(start = 12.dp, end = 12.dp),
            text = "Priority",
            style = MaterialTheme.typography.bodyLarge
        )
        NoteComposeTheme(viewModel = viewModel) {
            Slider(
                modifier = Modifier
                    .semantics { contentDescription = "Priority" }
                    .padding(8.dp),
                value = viewModel.priority,
                onValueChange = { viewModel.priorityChanged(it) },
                valueRange = 0f..10f,
                steps = 10
            )
        }
        Text(
            modifier = Modifier.padding(start = 12.dp, end = 12.dp),
            text = "Label",
            style = MaterialTheme.typography.bodyLarge
        )
        NoteComposeTheme(viewModel = viewModel) {
            FlowRow(
                modifier = Modifier
                    .padding(8.dp)
                    .navigationBarsPadding(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                labels.forEach {
                    LabelCard(
                        label = it.label,
                        selectedLabel = viewModel.label
                    ) { selectedLabel ->
                        viewModel.selectedLabelChanged(selectedLabel)
                    }
                }
            }
        }
    }
}