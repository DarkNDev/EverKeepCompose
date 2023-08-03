package com.darkndev.everkeepcompose.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darkndev.everkeepcompose.data.LabelDao
import com.darkndev.everkeepcompose.data.NoteDao
import com.darkndev.everkeepcompose.models.Note
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
    private val noteDao: NoteDao, state: SavedStateHandle, labelDao: LabelDao
) : ViewModel() {

    private val _allNotes = noteDao.getAllNotes()

    val allLabels = labelDao.getAllLabels()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedLabel = MutableStateFlow(state.get<String>("label") ?: ALL)
    val selectedLabel = _selectedLabel.asStateFlow()

    val allNotes = combine(selectedLabel, _allNotes) { label, notes ->
        Pair(label, notes)
    }.map { (label, notes) ->
        if (label == ALL) {
            notes
        } else {
            notes.filter {
                it.label == label
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

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

    companion object {
        const val ALL = "All"
    }
}