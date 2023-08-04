package com.darkndev.everkeepcompose.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.darkndev.everkeepcompose.models.Note
import com.darkndev.everkeepcompose.ui.home.HomeViewModel.Companion.ALL
import com.darkndev.everkeepcompose.ui.home.composable.LabelCard
import com.darkndev.everkeepcompose.ui.home.composable.NoteCard
import com.darkndev.everkeepcompose.ui.home.composable.SortSection
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigateNoteScreen: (note: Note?) -> Unit,
    navigateLabelScreen: () -> Unit
) {
    val notes by viewModel.allNotes.collectAsStateWithLifecycle()
    val labels by viewModel.allLabels.collectAsStateWithLifecycle()
    val selectedLabel by viewModel.selectedLabel.collectAsStateWithLifecycle()
    val sortOrder by viewModel.preferences.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var sortOrderSectionVisible by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "All Notes")
                },
                actions = {
                    IconButton(onClick = { navigateNoteScreen(null) }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Note"
                        )
                    }
                    IconButton(onClick = navigateLabelScreen) {
                        Icon(
                            imageVector = Icons.Default.Label,
                            contentDescription = "Manage Labels"
                        )
                    }
                    IconButton(onClick = { sortOrderSectionVisible = !sortOrderSectionVisible }) {
                        Icon(
                            imageVector = Icons.Default.Sort,
                            contentDescription = "Sort Notes"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            AnimatedVisibility(
                visible = sortOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                SortSection(sortOrder = sortOrder) {
                    viewModel.sortOrderChanged(it)
                }
            }
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                item {
                    LabelCard(label = ALL, selectedLabel = selectedLabel) { selectedLabel ->
                        viewModel.selectedLabelChanged(selectedLabel)
                    }
                }
                items(labels, key = { it.id }) {
                    LabelCard(label = it.label, selectedLabel) { selectedLabel ->
                        viewModel.selectedLabelChanged(selectedLabel)
                    }
                }
            }
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(notes, key = { it.id }) { note ->
                    NoteCard(
                        note = note,
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItemPlacement(
                                tween(300)
                            ),
                        navigate = { navigateNoteScreen(note) },
                        delete = {
                            viewModel.deleteNote(note) {
                                coroutineScope.launch {
                                    val snackBarResult = snackBarHostState.showSnackbar(
                                        message = "Note Deleted",
                                        actionLabel = "UNDO",
                                        duration = SnackbarDuration.Short
                                    )
                                    if (snackBarResult == SnackbarResult.ActionPerformed) {
                                        viewModel.undoClicked(it)
                                    }
                                }
                            }
                        })
                }
            }
        }
    }
}