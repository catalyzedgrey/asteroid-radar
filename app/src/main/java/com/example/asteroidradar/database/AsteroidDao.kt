package com.example.asteroidradar.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDao {
    @Query("select * from asteroid_table WHERE closeApproachDate >= :startDate ORDER BY closeApproachDate ASC")
    suspend fun getAllAsteroids(
        startDate: String,
    ): List<AsteroidEntity>

    @Query("SELECT * FROM asteroid_table WHERE closeApproachDate == :date")
    suspend fun getTodaysAsteroid(
        date: String,
    ): List<AsteroidEntity>

    //I didnt use te between operator here because we didnt want to include today's date.
    // We can just change the start date but i thought this would do just as well
    @Query("SELECT * FROM asteroid_table WHERE closeApproachDate > :startDate AND closeApproachDate <= :endDate ORDER BY closeApproachDate ASC")
    suspend fun getWeekAsteroids(
        startDate: String,
        endDate: String
    ): List<AsteroidEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: AsteroidEntity)
}


