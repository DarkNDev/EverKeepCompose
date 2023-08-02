package com.darkndev.everkeepcompose.ui.note

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    private val noteDao: NoteDao,
    state: SavedStateHandle
) : ViewModel() {

    val note = state.get<Note>("note")

    var title by mutableStateOf(state.get<String>("title") ?: note?.title ?: "")
        private set

    var content by mutableStateOf(state.get<String>("content") ?: note?.content ?: "")
        private set

    var colorRef by mutableIntStateOf(state.get<Int>("color") ?: note?.color ?: -1)
        private set

    fun titleChanged(titleText: String) {
        title = titleText
    }

    fun contentChanged(contentText: String) {
        content = contentText
    }

    fun colorChanged(colorReference: Int) {
        colorRef = colorReference
    }

    fun doneClicked(
        returnToHome: () -> Unit
    ) = viewModelScope.launch {
        if (title.isBlank() || content.isBlank()) return@launch
        if (note == null)
            noteDao.insertNote(Note(id = 0, title = title, content = content, color = colorRef))
        else
            noteDao.updateNote(note.copy(title = title, content = content, color = colorRef))
        returnToHome()
    }

    fun deleteClicked(returnToHome: () -> Unit) = viewModelScope.launch {
        note?.let { noteDao.deleteNote(it.id) }
        returnToHome()
    }
}