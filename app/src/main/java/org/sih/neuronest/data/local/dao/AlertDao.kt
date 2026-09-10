package org.sih.neuronest.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.sih.neuronest.data.local.entity.CaregiverAlert

@Dao
interface AlertDao {

    @Query("SELECT * FROM caregiver_alert ORDER BY timestamp DESC")
    fun getAllAlertsFlow(): Flow<List<CaregiverAlert>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: CaregiverAlert)

    @Query("UPDATE caregiver_alert SET isDismissed = 1 WHERE id = :alertId")
    suspend fun dismissAlert(alertId: String)
}
