package com.darkndev.everkeepcompose.data

import androidx.room.*
import com.darkndev.everkeepcompose.models.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Upsert
    suspend fun upsertNote(vararg note: Note)

    @Query("DELETE FROM note WHERE id=:noteId")
    suspend fun deleteNote(noteId: Int)

    @Query("SELECT * FROM note WHERE archived=:archived ORDER BY priority DESC")
    fun getAllNotes(archived:Boolean = false): Flow<List<Note>>

}