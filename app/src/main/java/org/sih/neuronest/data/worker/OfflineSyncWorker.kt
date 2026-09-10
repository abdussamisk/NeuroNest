package org.sih.neuronest.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import org.sih.neuronest.data.local.dao.CognitiveDao

@HiltWorker
class OfflineSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val cognitiveDao: CognitiveDao
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val unsynced = cognitiveDao.getUnsyncedSessions()
            for (session in unsynced) {
                // Simulate cloud synchronization push (Firebase / Retrofit API)
                cognitiveDao.markSessionSynced(session.id)
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
