package com.darkndev.everkeepcompose.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.darkndev.everkeepcompose.di.EverKeepScope
import com.darkndev.everkeepcompose.models.Label
import com.darkndev.everkeepcompose.models.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(
    entities = [Note::class, Label::class],
    version = 1,
    exportSchema = false
)
abstract class EverKeepDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    abstract fun labelDao(): LabelDao

    class Callback @Inject constructor(
        private val database: Provider<EverKeepDatabase>,
        @EverKeepScope private val everKeepScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            //val noteDao = database.get().noteDao()
            val labelDao = database.get().labelDao()
            everKeepScope.launch {
                /*noteDao.upsertNote(
                    Note(
                        id = 0,
                        title = "Hello World",
                        content = "This is my first Note"
                    ),
                    Note(
                        id = 0,
                        title = "Groceries",
                        content = "Sugar\nSalt\nHoney\nWheat\nRice\nDal",
                        label = "Groceries"
                    ),
                    Note(
                        id = 0,
                        title = "Vegetables",
                        content = "Tomato\nPotato\nBrinjal\nCabbage\nCauliflower",
                        label = "Shopping"
                    ),
                    Note(
                        id = 0,
                        title = "Fruits",
                        content = "Apple\nOrange\nMango\nBanana",
                        label = "Shopping"
                    )
                )*/
                labelDao.upsertLabel(Label(label = "Normal"))
                labelDao.upsertLabel(Label(label = "Shopping"))
                labelDao.upsertLabel(Label(label = "Groceries"))
                labelDao.upsertLabel(Label(label = "Gardening"))
                labelDao.upsertLabel(Label(label = "Events"))
                labelDao.upsertLabel(Label(label = "Projects"))
                labelDao.upsertLabel(Label(label = "Ideas"))
            }
        }
    }
}