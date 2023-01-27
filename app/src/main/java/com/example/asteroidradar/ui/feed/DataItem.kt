package com.example.asteroidradar.ui.feed

import com.example.asteroidradar.domain.models.Asteroid

sealed class DataItem {
    abstract val id: String

    data class AsteroidItem(val asteroid: Asteroid) : DataItem() {
        override val id: String = asteroid.id.toString()
    }

    object Header : DataItem() {
        override val id: String
            get() = Long.MIN_VALUE.toString()
    }
}