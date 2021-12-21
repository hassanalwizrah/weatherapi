package com.test.weatherapi.data.repo

import com.test.weatherapi.data.entities.DataModel
import com.test.weatherapi.data.remote.ApiResponse

interface DataSource {
    suspend fun getData(): ApiResponse<DataModel>
}