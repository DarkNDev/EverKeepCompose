package com.darkndev.everkeepcompose.ui.note

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.darkndev.everkeepcompose.models.Note
import com.darkndev.everkeepcompose.ui.note.composable.NoteColorButton
import com.darkndev.everkeepcompose.ui.note.composable.NoteTextField

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NoteScreen(
    viewModel: NoteViewModel,
    navigate: () -> Unit
) {
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
        val scrollState = rememberScrollState()
        val scrollColorState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .imePadding()
                .consumeWindowInsets(contentPadding)
                .systemBarsPadding()
                .verticalScroll(scrollState),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
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