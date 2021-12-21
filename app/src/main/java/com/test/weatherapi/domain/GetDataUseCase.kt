package com.test.weatherapi.domain

import com.test.weatherapi.data.remote.ServiceApi
import com.test.weatherapi.data.remote.safeApiCall
import javax.inject.Inject

class GetDataUseCase @Inject constructor(
    private val apiServiceApi: ServiceApi
) {
    suspend operator fun invoke() = safeApiCall {
        apiServiceApi.getData(
            //key = "mashri",
            key = "6e7bcade5f0a4a18aeb65644212112",
            q = "London",
            aqi = "no"
        )
    }
}