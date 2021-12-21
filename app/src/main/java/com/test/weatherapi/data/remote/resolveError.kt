package com.test.weatherapi.data.remote

import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.net.UnknownHostException

fun resolveError(throwable: Throwable): ApiResponse.Error {
    return when (throwable) {
        is HttpException -> resolveHttpException(throwable)
        is SerializationException -> onFailure(RequestException.parsingException(throwable))
        is UnknownHostException -> onFailure(RequestException.networkError(throwable))
        else -> onFailure(RequestException.unexpectedError(throwable))
    }
}

private fun onFailure(requestException: RequestException): ApiResponse.Error {
    return ApiResponse.Error(requestException.code, requestException.message)
}

private fun resolveHttpException(e: HttpException): ApiResponse.Error {
    val bodyErrorCode = e.code().toString()
    val bodyErrorMessage = e.message()

    return when (e.code()) {
        401 -> {
//            Application.setApplicationLevelObject(
//                ApplicationLevelAction.ApplicationLevelActionObject(true, bodyErrorCode, bodyErrorMessage)
//            )
            onFailure(RequestException.authenticationError(bodyErrorCode, bodyErrorMessage, e))
        }

        404 ->
            onFailure(RequestException.notFountError(e))

        400, 500, 501, 502, 503 ->
            onFailure(RequestException.serviceError(e.code(), bodyErrorCode, bodyErrorMessage, e))

        else ->
            onFailure(RequestException.httpException(e))
    }
}