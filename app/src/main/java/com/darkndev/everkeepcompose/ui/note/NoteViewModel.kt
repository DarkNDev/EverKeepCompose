package com.darkndev.everkeepcompose.ui.note

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darkndev.everkeepcompose.data.NoteDao
import com.darkndev.everkeepcompose.models.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteDao: NoteDao, private val state: SavedStateHandle
) : ViewModel() {

    val note = state.get<Note>("note")

    fun doneClicked(
        title: String?,
        content: String?,
        returnToHome: () -> Unit
    ) = viewModelScope.launch {
        if (title.isNullOrBlank() || content.isNullOrBlank()) return@launch
        if (note == null)
            noteDao.insertNote(Note(id = 0, title = title, content = content))
        else
            noteDao.updateNote(note.copy(title = title, content = content))
        returnToHome()
    }

    fun deleteClicked(returnToHome: () -> Unit) = viewModelScope.launch {
        note?.let { noteDao.deleteNote(it.id) }
        returnToHome()
    }
}