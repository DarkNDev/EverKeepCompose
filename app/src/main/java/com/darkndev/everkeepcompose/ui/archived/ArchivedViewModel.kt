package com.darkndev.everkeepcompose.ui.archived

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darkndev.everkeepcompose.data.NoteDao
import com.darkndev.everkeepcompose.models.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArchivedViewModel @Inject constructor(
    private val noteDao: NoteDao
) : ViewModel() {

    val archivedNotes = noteDao.getAllNotes(archived = true)

    fun undoClicked(note: Note) = viewModelScope.launch {
        noteDao.upsertNote(note)
    }

    fun deleteNote(note: Note, showMessage: (Note) -> Unit) = viewModelScope.launch {
        noteDao.deleteNote(note.id)
        showMessage(note.copy(id = 0))
    }
}