package org.sih.neuronest.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.sih.neuronest.data.local.entity.MemoryCue
import org.sih.neuronest.data.local.entity.RoutineReminder

@Dao
interface RoutineDao {

    @Query("SELECT * FROM routine_reminder ORDER BY scheduledTimeFormatted ASC")
    fun getAllRemindersFlow(): Flow<List<RoutineReminder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: RoutineReminder)

    @Query("UPDATE routine_reminder SET isAcknowledged = 1 WHERE id = :reminderId")
    suspend fun acknowledgeReminder(reminderId: String)

    @Query("SELECT * FROM memory_cue")
    fun getAllMemoryCuesFlow(): Flow<List<MemoryCue>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemoryCue(cue: MemoryCue)
}
