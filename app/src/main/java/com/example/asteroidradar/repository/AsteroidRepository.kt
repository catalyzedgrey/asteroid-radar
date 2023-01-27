package com.example.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
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

    val asteroidList: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAllAsteroids()) {
            it.asDomainModel()
        }

    suspend fun getAsteroids(
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
