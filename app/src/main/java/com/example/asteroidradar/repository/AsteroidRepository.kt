package com.example.asteroidradar.repository

import com.example.asteroidradar.database.AsteroidDatabase
import com.example.asteroidradar.database.AsteroidEntity
import com.example.asteroidradar.domain.models.Asteroid
import com.example.asteroidradar.network.Network
import com.example.asteroidradar.network.getSeventhDay
import com.example.asteroidradar.network.getToday
import com.example.asteroidradar.network.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject

class AsteroidRepository(private val database: AsteroidDatabase) {
    suspend fun refreshAsteroids(
        startDate: String = getToday(),
        endDate: String = getSeventhDay()
    ) {
        return withContext(Dispatchers.IO) {
            val asteroidResponseBody: ResponseBody = Network.service.getFeed(
                startDate,
                endDate
            )
            val asteroidsList = parseAsteroidsJsonResult(JSONObject(asteroidResponseBody.string()))
            database.asteroidDao.insertAll(*asteroidsList.asDatabaseModel())
        }
    }

    suspend fun getAllAsteroids(): List<Asteroid> {
        return withContext(Dispatchers.IO) {
            database.asteroidDao.getAllAsteroids(getToday()).asDomainModel()
        }
    }

    suspend fun getAsteroidsByDate(
        startDate: String = getToday(),
        endDate: String = getSeventhDay()
    ): List<Asteroid> {
        return withContext(Dispatchers.IO) {
            database.asteroidDao.getWeekAsteroids(
                startDate,
                endDate
            ).asDomainModel()
        }
    }

    suspend fun getAsteroidToday(
    ): List<Asteroid> {
        return withContext(Dispatchers.IO) {
            database.asteroidDao.getTodaysAsteroid(
                date = getToday()
            ).asDomainModel()
        }
    }
}

fun List<AsteroidEntity>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}

fun List<Asteroid>.asDatabaseModel(): Array<AsteroidEntity> {
    return map {
        AsteroidEntity(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()
}
