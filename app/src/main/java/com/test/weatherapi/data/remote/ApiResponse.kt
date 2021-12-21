package com.test.weatherapi.data.remote

sealed class ApiResponse<out T : Any> {
    data class Success<T : Any>(val items: T?) : ApiResponse<T>()
    data class Error(val errorCode: Int, val errorMessage: String) : ApiResponse<Nothing>()
    data class Progress(val progress: Boolean) : ApiResponse<Nothing>()
}