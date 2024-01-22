package com.achmea.demo

import android.app.Application
import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.achmea.demo.data.worker.DatabaseCleanerWorker
import com.achmea.demo.di.AppModule
import com.achmea.demo.di.AppModuleImpl
import java.util.concurrent.TimeUnit

class MyApplication : Application() {

    companion object {
        lateinit var appModule: AppModule
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        appModule = AppModuleImpl(this)
        scheduleLocalDBCleanerWorker()
    }

    private fun scheduleLocalDBCleanerWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicWorkRequest = PeriodicWorkRequestBuilder<DatabaseCleanerWorker>(
            repeatInterval = 7, // repeat every 7 days
            repeatIntervalTimeUnit = TimeUnit.DAYS
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueue(periodicWorkRequest)
    }
}