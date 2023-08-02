package com.darkndev.everkeepcompose.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.darkndev.everkeepcompose.di.EverKeepScope
import com.darkndev.everkeepcompose.models.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(
    entities = [Note::class],
    version = 1,
    exportSchema = false
)
abstract class EverKeepDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    class Callback @Inject constructor(
        private val database: Provider<EverKeepDatabase>,
        @EverKeepScope private val everKeepScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val noteDao = database.get().noteDao()
            everKeepScope.launch {
                noteDao.insertNote(
                    Note(id = 0, title = "Hello World", content = "This is my first Note"),
                    Note(id = 0, title = "Groceries", content = "Sugar\nSalt\nHoney\nWheat\nRice\nDal"),
                    Note(id = 0, title = "Vegetables", content = "Tomato\nPotato\nBrinjal\nCabbage\nCauliflower"),
                    Note(id = 0, title = "Fruits", content = "Apple\nOrange\nMango\nBanana")
                )
            }
        }
    }
}