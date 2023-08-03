package com.darkndev.everkeepcompose.ui.note

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.darkndev.everkeepcompose.ui.note.composable.NoteCustomiseSection
import com.darkndev.everkeepcompose.ui.note.composable.NoteTextField
import com.darkndev.everkeepcompose.ui.theme.EverKeepComposeTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NoteScreen(
    viewModel: NoteViewModel,
    navigate: () -> Unit
) {
    val scrollState = rememberScrollState()
    var showBottomSheet by remember { mutableStateOf(false) }
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
                NoteCustomiseSection(viewModel = viewModel) {
                    showBottomSheet = false
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