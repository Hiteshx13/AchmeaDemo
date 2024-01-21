package com.achmea.demo.data.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.achmea.demo.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseCleanerWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            Log.d("#CleaningDatabase", "Done...")
            MyApplication.appModule.employerRepository.deleteExpiredDataFromLocalDB()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}