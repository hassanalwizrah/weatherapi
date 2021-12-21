package com.test.weatherapi.data.remote

import kotlinx.coroutines.flow.flow

inline fun <T: Any> safeApiCall(
    crossinline call: suspend () -> T
) = flow {
    emit(ApiResponse.Progress(true))

    try {
        val response = call()
        emit(ApiResponse.Progress(false))
        emit(ApiResponse.Success(response))
    } catch (throwable: Throwable) {
        emit(ApiResponse.Progress(false))
        emit(resolveError(throwable))
    }
}