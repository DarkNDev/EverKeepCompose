package com.darkndev.everkeepcompose.ui.archived

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.darkndev.everkeepcompose.models.Note
import com.darkndev.everkeepcompose.ui.home.composable.NoteCard
import com.darkndev.everkeepcompose.ui.home.composable.NoteSearchBar
import kotlinx.coroutines.launch

@Composable
fun ArchivedScreen(
    viewModel: ArchivedViewModel,
    navigateNoteScreen: (Note) -> Unit,
    navigateBack: () -> Unit
) {
    val archivedNotes by viewModel.archivedNotes.collectAsStateWithLifecycle(initialValue = emptyList())
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var searchBarState by remember {
        mutableStateOf(false)
    }
    var queryText by remember {
        mutableStateOf("")
    }
    BackHandler(enabled = searchBarState) {
        queryText = ""
        searchBarState = false
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
                        Text(text = "Archived Notes")
                    },
                    navigationIcon = {
                        IconButton(onClick = navigateBack) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Return"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            searchBarState = !searchBarState
                        }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        }
                    }
                )
            }
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(archivedNotes.filter {
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