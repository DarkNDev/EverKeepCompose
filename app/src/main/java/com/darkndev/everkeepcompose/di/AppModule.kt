package com.darkndev.everkeepcompose.di

import android.content.Context
import androidx.room.Room
import com.darkndev.everkeepcompose.data.EverKeepDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context,
        callback: EverKeepDatabase.Callback
    ) = Room.databaseBuilder(context, EverKeepDatabase::class.java, "everKeep_database")
        .fallbackToDestructiveMigration()
        .addCallback(callback)
        .build()

    @Singleton
    @Provides
    fun provideDao(database: EverKeepDatabase) = database.noteDao()

    @EverKeepScope
    @Provides
    @Singleton
    fun provideEverKeepScope() = CoroutineScope(SupervisorJob())
}