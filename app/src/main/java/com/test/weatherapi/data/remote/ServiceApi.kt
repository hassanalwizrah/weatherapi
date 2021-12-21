package com.test.weatherapi.data.remote

import com.test.weatherapi.data.entities.DataModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceApi {

    @GET("current.json")
    suspend fun getData(
        @Query("key") key: String,
        @Query("q") q: String,
        @Query("aqi") aqi: String,
    ): DataModel
}