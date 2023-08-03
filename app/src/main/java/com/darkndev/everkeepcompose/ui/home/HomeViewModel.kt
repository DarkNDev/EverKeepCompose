package com.darkndev.everkeepcompose.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darkndev.everkeepcompose.data.LabelDao
import com.darkndev.everkeepcompose.data.NoteDao
import com.darkndev.everkeepcompose.data.PreferencesManager
import com.darkndev.everkeepcompose.models.Note
import com.darkndev.everkeepcompose.utils.SortOrder
import com.darkndev.everkeepcompose.utils.SortOrder.PRIORITY
import com.darkndev.everkeepcompose.utils.SortOrder.TIME
import com.darkndev.everkeepcompose.utils.SortOrder.TITLE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val noteDao: NoteDao,
    labelDao: LabelDao,
    state: SavedStateHandle,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _allNotes = noteDao.getAllNotes()

    val allLabels = labelDao.getAllLabels()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val preferences = preferencesManager.preferencesFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, PRIORITY)

    private val _selectedLabel = MutableStateFlow(state.get<String>("label") ?: ALL)
    val selectedLabel = _selectedLabel.asStateFlow()

    val allNotes = combine(selectedLabel, preferences, _allNotes) { label, preferences, notes ->
        Triple(label, preferences, notes)
    }.map { (label, preferences, notes) ->
        val filteredNotes = if (label == ALL) {
            notes
        } else {
            notes.filter {
                it.label == label
            }
        }
        when (preferences) {
            TIME -> filteredNotes.sortedByDescending { it.timestamp }
            PRIORITY -> filteredNotes.sortedByDescending { it.priority }
            TITLE -> filteredNotes.sortedBy { it.title }
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun undoClicked(note: Note) = viewModelScope.launch {
        noteDao.upsertNote(note)
    }

    fun deleteNote(note: Note, showMessage: (Note) -> Unit) = viewModelScope.launch {
        noteDao.deleteNote(note.id)
        showMessage(note.copy(id = 0))
    }

    fun selectedLabelChanged(selected: String) {
        _selectedLabel.value = selected
    }

    fun sortOrderChanged(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }

    companion object {
        const val ALL = "All"
    }
}