package com.example.asteroidradar.network

import com.example.asteroidradar.Constants.API_KEY
import com.example.asteroidradar.Constants.BASE_URL
import com.example.asteroidradar.Constants.DEFAULT_END_DATE_DAYS
import com.example.asteroidradar.domain.models.PictureOfDay
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(value = "/neo/rest/v1/feed")
    suspend fun getFeed(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String = API_KEY
    ): ResponseBody

    @GET(value = "planetary/apod")
    suspend fun getNasaImageOfTheDay(
        @Query("api_key") apiKey: String = API_KEY
    ): PictureOfDay

}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object Network {
    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val service = retrofit.create(ApiService::class.java)
}