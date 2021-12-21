package com.test.weatherapi.data.repo

import com.test.weatherapi.data.entities.DataModel
import com.test.weatherapi.data.remote.ApiResponse
import com.test.weatherapi.data.remote.ServiceApi
import com.test.weatherapi.data.remote.resolveError
import kotlinx.coroutines.Deferred
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class DataSourceImpl @Inject constructor(
    private val apiServiceApi: ServiceApi
) : DataSource {

    override suspend fun getData(): ApiResponse<DataModel> {
        return safeApiCall {
            apiServiceApi.getData(
                //key = "hassan",
                key = "6e7bcade5f0a4a18aeb65644212112",
                q = "London",
                aqi = "no"
            )
        }
    }
}

suspend fun <T : Any> safeApiCall(
    call: suspend () -> Deferred<Response<T>>
): ApiResponse<T> {

    return try {
        val response = call().await()

        return if (response.isSuccessful) ApiResponse.Success(response.body())
        else ApiResponse.Error(response.code(), response.message())

    } catch (e: Exception) {
        resolveError(e)
    }
}