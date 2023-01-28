package com.example.asteroidradar.ui.feed

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.asteroidradar.database.AsteroidDatabase
import com.example.asteroidradar.domain.models.Asteroid
import com.example.asteroidradar.domain.models.PictureOfDay
import com.example.asteroidradar.network.Network
import com.example.asteroidradar.network.getSeventhDay
import com.example.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

class FeedViewModel(application: Application) : ViewModel() {
    private val database = AsteroidDatabase.getInstance(application)
    private val repository = AsteroidRepository(database)

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
        filterAsteroidsByAll()
    }

    fun filterAsteroidsByAll() {
        viewModelScope.launch {
            try {
                _asteroids.value = repository.getAllAsteroids()
                //Refresh the list if the work manager hasnt run at all when you open the app
                if (_asteroids.value?.isEmpty() == true) {
                    repository.refreshAsteroids()
                    _asteroids.value = repository.getAllAsteroids()
                }
            } catch (exception: java.lang.Exception) {
                Log.e("FeedViewModel", exception.stackTraceToString())
            }
        }
    }

    fun filterAsteroidsByToday() {
        viewModelScope.launch {
            try {
                _asteroids.value = repository.getAsteroidToday()
            } catch (exception: java.lang.Exception) {
                Log.e("FeedViewModel", exception.stackTraceToString())
            }
        }
    }

    fun filterAsteroidsByWeek() {
        viewModelScope.launch {
            try {
                _asteroids.value = repository.getAsteroidsByDate()
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