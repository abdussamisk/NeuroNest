package org.sih.neuronest.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import org.sih.neuronest.data.local.dao.AlertDao
import org.sih.neuronest.data.local.dao.CognitiveDao
import org.sih.neuronest.data.local.dao.RoutineDao
import org.sih.neuronest.data.local.entity.*

@Database(
    entities = [
        PatientProfile::class,
        CognitiveSession::class,
        RoutineReminder::class,
        CaregiverAlert::class,
        MemoryCue::class
    ],
    version = 1,
    exportSchema = false
)
abstract class NeuroNestDatabase : RoomDatabase() {
    abstract fun cognitiveDao(): CognitiveDao
    abstract fun routineDao(): RoutineDao
    abstract fun alertDao(): AlertDao
}
