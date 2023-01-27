package com.example.asteroidradar.repository

import com.example.asteroidradar.domain.models.Asteroid
import com.example.asteroidradar.network.Network
import com.example.asteroidradar.network.getSeventhDay
import com.example.asteroidradar.network.getToday
import com.example.asteroidradar.network.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRepository {

    suspend fun getAsteroids(
        startDate: String = getToday(),
        endDate: String = getSeventhDay()
    ): List<Asteroid> {
        return withContext(Dispatchers.IO) {
            val asteroidResponseBody: ResponseBody = Network.service.getFeed(
                startDate,
                endDate
            )
            parseAsteroidsJsonResult(JSONObject(asteroidResponseBody.string()))
        }
    }
}