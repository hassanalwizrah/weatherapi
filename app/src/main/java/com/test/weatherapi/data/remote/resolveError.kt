package com.test.weatherapi.data.remote

import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.net.UnknownHostException

fun resolveError(throwable: Throwable): ApiResponse.Error {
    return when (throwable) {
        is HttpException -> resolveHttpException(throwable)
        is SerializationException -> RequestException.parsingException(throwable).asFailure()
        is UnknownHostException -> RequestException.networkError(throwable).asFailure()
        else -> RequestException.unexpectedError(throwable).asFailure()
    }
}

private fun resolveHttpException(e: HttpException): ApiResponse.Error {
    val bodyErrorCode = e.code().toString()
    val bodyErrorMessage = e.message()

    return when (e.code()) {
        401 -> {
//            Application.setApplicationLevelObject(
//                ApplicationLevelAction.ApplicationLevelActionObject(true, bodyErrorCode, bodyErrorMessage)
//            )
            RequestException.authenticationError(bodyErrorCode, bodyErrorMessage, e).asFailure()
        }
        404 -> RequestException.notFountError(e).asFailure()
        400, 500, 501, 502, 503 -> RequestException.serviceError(e.code(), bodyErrorCode, bodyErrorMessage, e).asFailure()
        else -> RequestException.httpException(e).asFailure()
    }
}

private fun RequestException.asFailure() = ApiResponse.Error(this)