package com.test.weatherapi.data.remote

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import java.lang.Exception
import java.net.UnknownHostException

fun resolveError(exception: Exception): ApiResponse.Error {
    return when (exception) {
        is HttpException -> resolveHttpException(exception)
        is JsonSyntaxException -> onFailure(RequestException.parsingException(exception))
        is UnknownHostException -> onFailure(RequestException.networkError(exception))
        else -> onFailure(RequestException.unexpectedError(exception))
    }
}

private fun onFailure(requestException: RequestException): ApiResponse.Error {
    return ApiResponse.Error(requestException.code, requestException.message)
}

private fun resolveHttpException(e: HttpException): ApiResponse.Error {
    var errorResponse: JsonObject? = null

    try {
        errorResponse = Gson().fromJson(e.response()?.errorBody()?.string(), JsonObject::class.java)
    } catch (js: Exception) {
        //sentery
        //FirebaseCrashlytics.getInstance().log("**body**${e.response().errorBody()?.string()}**error**${js.message}")
    }

    val errorJson = errorResponse?.getAsJsonArray("errors")?.get(0)?.asJsonObject
    val bodyErrorCode = errorJson?.get("code")?.asString.orEmpty()
    val bodyErrorMessage = errorJson?.get("message")?.asString ?: e.message().orEmpty()

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