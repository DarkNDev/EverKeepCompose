package com.darkndev.everkeepcompose.data

import androidx.room.*
import com.darkndev.everkeepcompose.models.Label
import kotlinx.coroutines.flow.Flow

@Dao
interface LabelDao {

    @Upsert
    suspend fun upsertLabel(label: Label)

    @Delete
    suspend fun deleteLabel(label: Label)

    @Query("SELECT * FROM label")
    fun getAllLabels(): Flow<List<Label>>

}