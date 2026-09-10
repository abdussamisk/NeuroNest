package org.sih.neuronest.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.sih.neuronest.data.local.NeuroNestDatabase
import org.sih.neuronest.data.local.dao.AlertDao
import org.sih.neuronest.data.local.dao.CognitiveDao
import org.sih.neuronest.data.local.dao.RoutineDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NeuroNestDatabase {
        return Room.databaseBuilder(
            context,
            NeuroNestDatabase::class.java,
            "neuronest_db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideCognitiveDao(db: NeuroNestDatabase): CognitiveDao = db.cognitiveDao()

    @Provides
    fun provideRoutineDao(db: NeuroNestDatabase): RoutineDao = db.routineDao()

    @Provides
    fun provideAlertDao(db: NeuroNestDatabase): AlertDao = db.alertDao()
}
