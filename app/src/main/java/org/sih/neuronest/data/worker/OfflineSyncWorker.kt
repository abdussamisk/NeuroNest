package org.sih.neuronest.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import org.sih.neuronest.data.local.dao.CognitiveDao
import org.sih.neuronest.data.remote.supabase.SupabaseRemoteDataSource

@HiltWorker
class OfflineSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val cognitiveDao: CognitiveDao,
    private val supabaseRemoteDataSource: SupabaseRemoteDataSource
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val unsyncedSessions = cognitiveDao.getUnsyncedSessions()
            if (unsyncedSessions.isNotEmpty()) {
                val success = supabaseRemoteDataSource.syncCognitiveSessions(unsyncedSessions)
                if (success) {
                    for (session in unsyncedSessions) {
                        cognitiveDao.markSessionSynced(session.id)
                    }
                } else {
                    return Result.retry()
                }
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
