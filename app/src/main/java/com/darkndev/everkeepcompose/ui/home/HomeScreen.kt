package com.darkndev.everkeepcompose.ui.home

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.darkndev.everkeepcompose.R
import com.darkndev.everkeepcompose.models.Note
import com.darkndev.everkeepcompose.ui.home.HomeViewModel.Companion.ALL
import com.darkndev.everkeepcompose.ui.home.composable.NoteSearchBar
import com.darkndev.everkeepcompose.ui.home.composable.LabelCard
import com.darkndev.everkeepcompose.ui.home.composable.NoteCard
import com.darkndev.everkeepcompose.ui.home.composable.SortSection
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigateNoteScreen: (note: Note?) -> Unit,
    navigateLabelScreen: () -> Unit,
    navigateArchivedScreen: () -> Unit
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
    var searchBarState by remember {
        mutableStateOf(false)
    }
    var queryText by remember {
        mutableStateOf("")
    }
    BackHandler(enabled = searchBarState || sortOrderSectionVisible) {
        queryText = ""
        searchBarState = false
        sortOrderSectionVisible = false
    }
    var dropDownMenuState by remember {
        mutableStateOf(false)
    }
    var showDialog by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            if (searchBarState) {
                NoteSearchBar(
                    queryText = queryText,
                    onQueryTextChange = { queryText = it },
                    closeSearchOnClick = {
                        queryText = ""
                        searchBarState = !searchBarState
                    },
                    clearTextOnClick = { queryText = "" }
                )
            } else {
                TopAppBar(
                    title = {
                        Text(text = "All Notes")
                    },
                    actions = {
                        IconButton(onClick = {
                            searchBarState = !searchBarState
                            sortOrderSectionVisible = false
                        }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        }
                        IconButton(onClick = {
                            sortOrderSectionVisible = !sortOrderSectionVisible
                            searchBarState = false
                        }) {
                            Icon(
                                imageVector = Icons.Default.Sort,
                                contentDescription = "Sort Notes"
                            )
                        }
                        IconButton(onClick = { dropDownMenuState = !dropDownMenuState }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Others"
                            )
                        }
                        DropdownMenu(
                            expanded = dropDownMenuState,
                            onDismissRequest = { dropDownMenuState = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(text = "Labels") },
                                onClick = {
                                    dropDownMenuState = false
                                    navigateLabelScreen()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = "Archived") },
                                onClick = {
                                    dropDownMenuState = false
                                    navigateArchivedScreen()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = "About") },
                                onClick = {
                                    dropDownMenuState = false
                                    showDialog = !showDialog
                                }
                            )
                        }
                    }
                )
            }
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
        floatingActionButton = {
            val orientation = LocalConfiguration.current.orientation
            FloatingActionButton(
                modifier = if (orientation == Configuration.ORIENTATION_LANDSCAPE) Modifier.navigationBarsPadding() else Modifier,
                onClick = { navigateNoteScreen(null) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.background
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Note"
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { contentPadding ->
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(
                        onClick = { showDialog = false }
                    ) {
                        Text(text = "Ok")
                    }
                },
                title = { Text(text = "About EverKeep") },
                text = { Text(text = stringResource(id = R.string.app_description)) }
            )
        }
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
                items(notes.filter {
                    if (queryText.isBlank()) true else it.title.contains(queryText, true)
                }, key = { it.id }) { note ->
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