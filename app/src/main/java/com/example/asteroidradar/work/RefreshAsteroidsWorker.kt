package com.example.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.asteroidradar.database.AsteroidDatabase
import com.example.asteroidradar.network.getEigthDay
import com.example.asteroidradar.network.getTomorrow
import com.example.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException

class RefreshAsteroidsWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {
    companion object {
        const val WORK_NAME = "RefreshAsteroidsWorker"
    }

    override suspend fun doWork(): Result {
        val database = AsteroidDatabase.getInstance(context = applicationContext)
        val repository = AsteroidRepository(database)

        return try {
            repository.refreshAsteroids(startDate = getTomorrow(), endDate = getEigthDay())
            Result.success()
        } catch (exception: HttpException) {
            return Result.retry()
        }
    }
}