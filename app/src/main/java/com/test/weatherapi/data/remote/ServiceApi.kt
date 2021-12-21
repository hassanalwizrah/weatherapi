package com.test.weatherapi.data.remote

import com.test.weatherapi.data.entities.DataModel
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceApi {


    @GET("current.json")
    fun getData(
        @Query("key") key: String,
        @Query("q") q: String,
        @Query("aqi") aqi: String,
    ): Deferred<Response<DataModel>>
}