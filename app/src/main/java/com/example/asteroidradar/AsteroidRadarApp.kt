package com.example.asteroidradar

import android.app.Application
import android.os.Build
import androidx.work.*
import com.example.asteroidradar.work.RefreshAsteroidsWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AsteroidRadarApp : Application() {
    val applicationScope = CoroutineScope(Dispatchers.Default)

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
                .apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        setRequiresDeviceIdle(true)
                    }
                }.build()

            val request = OneTimeWorkRequestBuilder<RefreshAsteroidsWorker>()
                .setConstraints(constraints)
                .build()


            WorkManager.getInstance(this@AsteroidRadarApp)
                .enqueue(request)
        }
    }
}