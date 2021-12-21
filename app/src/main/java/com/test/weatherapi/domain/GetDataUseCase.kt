package com.test.weatherapi.domain

import com.test.weatherapi.data.entities.DataModel
import com.test.weatherapi.data.remote.ApiResponse
import com.test.weatherapi.data.repo.DataSourceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetDataUseCase @Inject constructor(
    private val dataSource: DataSourceImpl
){

    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        flow<ApiResponse<DataModel>> {
            emit(ApiResponse.Progress(true))

            dataSource.getData().let {
                emit(ApiResponse.Progress(false))
                emit(it)
            }
        }
    }
}