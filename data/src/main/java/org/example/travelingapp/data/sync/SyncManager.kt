package org.example.travelingapp.data.sync

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.gson.Gson
import org.example.travelingapp.data.local.daos.PendingOperationDao
import org.example.travelingapp.data.local.entities.PendingOperationEntity
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages the offline operation queue and triggers WorkManager sync.
 *
 * Usage from any repository:
 *   syncManager.enqueue("CREATE", "Destination", id, request)
 *
 * WorkManager will process the queue when network is available.
 */
@Singleton
class SyncManager @Inject constructor(
    private val pendingOperationDao: PendingOperationDao,
    private val context: Context
) {
    private val gson = Gson()

    suspend fun enqueue(operationType: String, entityType: String, entityId: String, payload: Any? = null) {
        val json = if (payload != null) gson.toJson(payload) else "{}"

        pendingOperationDao.insert(
            PendingOperationEntity(
                operationType = operationType,
                entityType = entityType,
                entityId = entityId,
                payload = json
            )
        )

        triggerSync()
    }

    fun triggerSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                SyncWorker.WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                syncRequest
            )
    }

    suspend fun pendingCount(): Int = pendingOperationDao.count()
}
