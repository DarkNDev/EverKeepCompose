package com.darkndev.everkeepcompose.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.darkndev.everkeepcompose.utils.getFormatTime
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val color: Int = colorReference.random()
) : Parcelable {
    fun toTime() = getFormatTime(timestamp)

    companion object {
        val colorReference = listOf(0, 1, 2, 3, 4, 5, 6, 7)
    }
}
