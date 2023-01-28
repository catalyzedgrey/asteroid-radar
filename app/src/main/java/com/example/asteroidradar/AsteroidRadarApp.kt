package com.example.asteroidradar

import android.app.Application
import android.os.Build
import androidx.work.*
import com.example.asteroidradar.work.RefreshAsteroidsWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AsteroidRadarApp : Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }

    private fun delayedInit() {
        applicationScope.launch {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(true)
                .build()

            val repeatingRequest = PeriodicWorkRequestBuilder<RefreshAsteroidsWorker>(
                1,
                TimeUnit.DAYS,
            )
                .setConstraints(constraints)
                .build()


            WorkManager.getInstance(this@AsteroidRadarApp).enqueueUniquePeriodicWork(
                RefreshAsteroidsWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                repeatingRequest
            )
        }
    }
}