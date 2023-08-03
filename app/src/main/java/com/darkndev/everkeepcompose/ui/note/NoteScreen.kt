package com.darkndev.everkeepcompose.ui.note

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.darkndev.everkeepcompose.models.Note
import com.darkndev.everkeepcompose.ui.home.composable.LabelCard
import com.darkndev.everkeepcompose.ui.note.composable.NoteColorButton
import com.darkndev.everkeepcompose.ui.note.composable.NoteTextField
import com.darkndev.everkeepcompose.ui.theme.EverKeepComposeTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NoteScreen(
    viewModel: NoteViewModel,
    navigate: () -> Unit
) {
    val scrollState = rememberScrollState()
    val scrollColorState = rememberScrollState()
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val labels by viewModel.allLabels.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "")
                },
                actions = {
                    IconButton(onClick = { viewModel.doneClicked(navigate) }) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "Done"
                        )
                    }
                    IconButton(onClick = { viewModel.deleteClicked(navigate) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete"
                        )
                    }
                    IconButton(onClick = { showBottomSheet = true }) {
                        Icon(
                            imageVector = Icons.Default.Sort,
                            contentDescription = "Options"
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navigate() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Return"
                        )
                    }
                }
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { contentPadding ->
        if (showBottomSheet) {
            EverKeepComposeTheme {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet = false
                    },
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
                    Slider(
                        modifier = Modifier
                            .semantics { contentDescription = "Priority" }
                            .padding(8.dp),
                        value = viewModel.priority,
                        onValueChange = { viewModel.priorityChanged(it) },
                        valueRange = 0f..10f,
                        steps = 10
                    )
                    Text(
                        modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                        text = "Label",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    FlowRow(
                        modifier = Modifier.padding(8.dp).navigationBarsPadding(),
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .imePadding()
                .consumeWindowInsets(contentPadding)
                .systemBarsPadding()
                .verticalScroll(scrollState),
        ) {
            NoteTextField(
                value = viewModel.title,
                onValueChange = { viewModel.titleChanged(it) },
                hintText = "Enter Title",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 8.dp),
                textStyle = MaterialTheme.typography.titleLarge,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                )
            )
            NoteTextField(
                value = viewModel.content,
                onValueChange = { viewModel.contentChanged(it) },
                hintText = "Enter Content",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 20.dp, end = 20.dp, bottom = 8.dp),
                textStyle = MaterialTheme.typography.bodyLarge,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
            )
        }
    }
}