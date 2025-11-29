package com.example.habitstracker.di

import android.content.Context
import com.example.habitstracker.data.database.HabitDatabase
import com.example.habitstracker.data.repository.HabitRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideHabitDatabase(@ApplicationContext context: Context): HabitDatabase {
        return HabitDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideHabitRepository(database: HabitDatabase): HabitRepository {
        return HabitRepository(database.habitDao())
    }
}