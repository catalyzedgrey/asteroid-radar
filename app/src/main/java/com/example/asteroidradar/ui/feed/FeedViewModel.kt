package com.example.asteroidradar.ui.feed

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asteroidradar.domain.models.Asteroid
import com.example.asteroidradar.domain.models.PictureOfDay
import com.example.asteroidradar.network.Network
import com.example.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

class FeedViewModel : ViewModel() {
    val repository = AsteroidRepository()

    private val _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

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
                _asteroids.value = repository.getAsteroids()
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