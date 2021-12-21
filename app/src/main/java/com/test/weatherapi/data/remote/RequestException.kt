package com.test.weatherapi.data.remote

import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import java.util.*

class RequestException(
    val code: Int = -1,
    val bodyErrorCode: String = "",
    val kind: Kind = Kind.NONE,
    override val message: String,
    exception: Throwable
) : RuntimeException(message, exception) {

    /**
     * Identifies the event kind which triggered a [RequestException].
     */
    enum class Kind {
        AUTHENTICATION_ERROR,
        NOT_FOUND_ERROR,
        SERVICE_ERROR,
        UNEXPECTED,
        JSON_PARSING,
        NO_NETWORK_ERROR,
        TIMEOUT_ERROR,
        NONE
    }

    companion object {

        internal fun authenticationError(bodyErrorCode: String, message: String, exception: HttpException) =
            RequestException(401, bodyErrorCode, Kind.AUTHENTICATION_ERROR, message, exception)

        internal fun notFountError(exception: HttpException) =
            RequestException(404, "404", Kind.NOT_FOUND_ERROR, getErrorString(Kind.NOT_FOUND_ERROR), exception)

        internal fun serviceError(httpCode: Int, bodyCode: String, message: String, exception: HttpException) =
            RequestException(httpCode, bodyCode,  Kind.SERVICE_ERROR, message, exception)

        internal fun httpException(exception: HttpException) =
            RequestException(exception.code(), exception.code().toString(), Kind.UNEXPECTED,  getErrorString(Kind.UNEXPECTED), exception)

        internal fun timeoutError(exception: IOException) =
            RequestException(kind = Kind.TIMEOUT_ERROR, message = getErrorString(Kind.TIMEOUT_ERROR), exception = exception)

        fun parsingException(je: SerializationException) =
            RequestException(kind = Kind.JSON_PARSING, message = getErrorString(Kind.JSON_PARSING), exception = je)

        internal fun networkError(exception: IOException) =
            RequestException(kind = Kind.NO_NETWORK_ERROR, message = getErrorString(Kind.NO_NETWORK_ERROR), exception = exception)

        internal fun unexpectedError(exception: Throwable) =
            RequestException(kind = Kind.UNEXPECTED, message = getErrorString(Kind.UNEXPECTED), exception = exception)

        private fun getErrorString(kind: Kind): String {
            //TODO: customize errors message
            return when (kind) {
                Kind.AUTHENTICATION_ERROR -> getErrorString("Something went wrong", "حدث خطا ما")
                Kind.SERVICE_ERROR -> getErrorString("Something went wrong", "حدث خطا ما")
                Kind.NOT_FOUND_ERROR -> getErrorString("Something went wrong", "حدث خطا ما")
                Kind.NO_NETWORK_ERROR -> getErrorString("Something went wrong", "حدث خطا ما")
                Kind.TIMEOUT_ERROR -> getErrorString("Something went wrong", "حدث خطا ما")
                Kind.UNEXPECTED -> getErrorString("Something went wrong", "حدث خطا ما")
                Kind.JSON_PARSING -> getErrorString("Something went wrong", "حدث خطا ما")
                Kind.NONE -> getErrorString("Something went wrong", "حدث خطا ما")
            }
        }

        private fun getErrorString(en: String, ar: String) = if (Locale.getDefault().language.compareTo("en", true) == 0) en else ar
    }
}