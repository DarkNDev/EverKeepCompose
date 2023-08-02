package com.darkndev.everkeepcompose.data

import androidx.room.*
import com.darkndev.everkeepcompose.models.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Upsert
    suspend fun insertNote(vararg note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Query("DELETE FROM note WHERE id=:noteId")
    suspend fun deleteNote(noteId: Int)

    @Query("SELECT * FROM note ORDER BY title")
    fun getAllNotes(): Flow<List<Note>>

}