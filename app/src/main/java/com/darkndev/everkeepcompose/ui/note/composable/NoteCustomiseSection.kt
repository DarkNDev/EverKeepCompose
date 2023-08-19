package com.darkndev.everkeepcompose.ui.note.composable

import android.widget.Toast
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Unarchive
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.darkndev.everkeepcompose.models.Note
import com.darkndev.everkeepcompose.ui.home.composable.LabelCard
import com.darkndev.everkeepcompose.ui.note.NoteViewModel
import com.darkndev.everkeepcompose.ui.theme.NoteComposeTheme

@Composable
fun NoteCustomiseSection(
    viewModel: NoteViewModel,
    onDismissRequest: () -> Unit,
    navigate: () -> Unit
) {
    val scrollColorState = rememberScrollState()
    val scrollSettingState = rememberScrollState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val labels by viewModel.allLabels.collectAsStateWithLifecycle()
    val context = LocalContext.current
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        windowInsets = WindowInsets(0, 0, 0, 0)
    ) {
        viewModel.note?.let { note ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollSettingState),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SettingItem(imageVector = Icons.Outlined.Share, contentDescription = "Share") {
                    onDismissRequest()
                    viewModel.shareClicked(context)
                }
                SettingItem(imageVector = Icons.Outlined.ContentCopy, contentDescription = "Copy") {
                    onDismissRequest()
                    viewModel.copyClicked {
                        Toast.makeText(context, "Note copy created", Toast.LENGTH_SHORT).show()
                    }
                }
                SettingItem(imageVector = Icons.Outlined.Delete, contentDescription = "Delete") {
                    onDismissRequest()
                    viewModel.deleteClicked(navigate)
                }
                SettingItem(
                    imageVector = if (note.archived) Icons.Outlined.Unarchive else Icons.Outlined.Archive,
                    contentDescription = if (note.archived) "Unarchive" else "Archive"
                ) {
                    onDismissRequest()
                    viewModel.archiveClicked(navigate)
                }
            }
            Divider(
                modifier = Modifier.padding(
                    top = 24.dp,
                    bottom = 16.dp
                )
            )
        }
        Text(
            modifier = Modifier.padding(12.dp),
            text = "Color",
            style = MaterialTheme.typography.titleMedium
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
            modifier = Modifier.padding(12.dp),
            text = "Priority",
            style = MaterialTheme.typography.titleMedium
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
            modifier = Modifier.padding(12.dp),
            text = "Label",
            style = MaterialTheme.typography.titleMedium
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