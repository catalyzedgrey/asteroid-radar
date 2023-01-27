package com.example.asteroidradar.ui.feed

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asteroidradar.database.AsteroidDatabase
import com.example.asteroidradar.domain.models.Asteroid
import com.example.asteroidradar.domain.models.PictureOfDay
import com.example.asteroidradar.network.Network
import com.example.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

class FeedViewModel(application: Application) : ViewModel() {
    private val database = AsteroidDatabase.getInstance(application)
    private val repository = AsteroidRepository(database)

    val asteroids = repository.asteroidList

    private val _nasaPictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _nasaPictureOfDay


    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid?>()
    val navigateToSelectedAsteroid: LiveData<Asteroid?>
        get() = _navigateToSelectedAsteroid

    init {
        getImageOfTheDay()
        getAsteroids()
    }

    private fun getAsteroids() {
        viewModelScope.launch {
            try {
                repository.getAsteroids()
            } catch (exception: java.lang.Exception) {
                Log.e("FeedViewModel", exception.stackTraceToString())
            }
        }
    }

    private fun getImageOfTheDay() {
        viewModelScope.launch {
            try {
                _nasaPictureOfDay.value = Network.service.getNasaImageOfTheDay()
            } catch (exception: java.lang.Exception) {
                Log.e("FeedViewModel", exception.stackTraceToString())
            }

        }
    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }

}