package com.darkndev.everkeepcompose.ui.note

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Unarchive
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.darkndev.everkeepcompose.ui.note.composable.NoteCustomiseSection
import com.darkndev.everkeepcompose.ui.note.composable.NoteTextField
import com.darkndev.everkeepcompose.ui.theme.EverKeepComposeTheme

@Composable
fun NoteScreen(
    viewModel: NoteViewModel,
    navigate: () -> Unit
) {
    val scrollState = rememberScrollState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var dropDownMenuState by remember {
        mutableStateOf(false)
    }
    var showDeleteDialog by remember {
        mutableStateOf(false)
    }
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
                    IconButton(onClick = { showBottomSheet = true }) {
                        Icon(
                            imageVector = Icons.Default.EditNote,
                            contentDescription = "Options"
                        )
                    }
                    viewModel.note?.let { note ->
                        IconButton(onClick = { dropDownMenuState = !dropDownMenuState }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More Options"
                            )
                        }
                        DropdownMenu(
                            expanded = dropDownMenuState,
                            onDismissRequest = { dropDownMenuState = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(text = "Share") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "Share"
                                    )
                                },
                                onClick = {
                                    dropDownMenuState = false
                                    viewModel.shareClicked(context)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = "Make a copy") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copy"
                                    )
                                },
                                onClick = {
                                    dropDownMenuState = false
                                    viewModel.copyClicked {
                                        Toast.makeText(
                                            context,
                                            "Note copy created",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = "Delete") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.DeleteOutline,
                                        contentDescription = "Share"
                                    )
                                },
                                onClick = {
                                    dropDownMenuState = false
                                    showDeleteDialog = !showDeleteDialog
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = if (note.archived) "Unarchive" else "Archive") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = if (note.archived) Icons.Outlined.Unarchive else Icons.Outlined.Archive,
                                        contentDescription = "Share"
                                    )
                                },
                                onClick = {
                                    dropDownMenuState = false
                                    viewModel.archiveClicked(navigate)
                                }
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = navigate) {
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
                NoteCustomiseSection(
                    viewModel = viewModel,
                    onDismissRequest = { showBottomSheet = false }
                )
            }
        }
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        showDeleteDialog = false
                        viewModel.deleteClicked(navigate)
                    }) {
                        Text(text = "Ok")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDeleteDialog = false
                    }) {
                        Text(text = "Cancel")
                    }
                },
                title = { Text(text = "Delete Note?") }
            )
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
            ElevatedAssistChip(
                modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                onClick = { showBottomSheet = !showBottomSheet },
                label = { Text(text = viewModel.label) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Label,
                        contentDescription = "Note Label"
                    )
                },
                colors = AssistChipDefaults.elevatedAssistChipColors(
                    containerColor = MaterialTheme.colorScheme.onSurface,
                    labelColor = MaterialTheme.colorScheme.surface,
                    leadingIconContentColor = MaterialTheme.colorScheme.surface,
                    trailingIconContentColor = MaterialTheme.colorScheme.surface
                )
            )
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