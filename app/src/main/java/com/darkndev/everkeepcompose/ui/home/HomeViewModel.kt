package com.darkndev.everkeepcompose.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darkndev.everkeepcompose.data.NoteDao
import com.darkndev.everkeepcompose.models.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val noteDao: NoteDao
) : ViewModel() {

    val allNotes = noteDao.getAllNotes()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun undoClicked(note: Note) = viewModelScope.launch {
        noteDao.insertNote(note)
    }

    fun deleteNote(note: Note, showMessage: (Note) -> Unit) = viewModelScope.launch {
        noteDao.deleteNote(note.id)
        showMessage(note.copy(id = 0))
    }
}