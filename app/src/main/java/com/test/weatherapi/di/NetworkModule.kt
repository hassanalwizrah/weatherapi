package com.test.weatherapi.di

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.test.weatherapi.BuildConfig
import com.test.weatherapi.data.remote.ServiceApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val CONNECT_TIMEOUT_IN_SECONDS = 10
    private const val READ_TIMEOUT_IN_SECONDS = 60
    private const val WRITE_TIMEOUT_IN_SECONDS = 60
    private const val CACHE_SIZE_BYTES = 10 * 1024 * 1024L // 10 MB
    private const val KEY = "6e7bcade5f0a4a18aeb65644212112"

    private val json = Json{
        isLenient = true
        ignoreUnknownKeys = true
    }

    /**
     * provides an Interceptor object to enable logging http request/response,
     * based on the defined log level
     *
     */
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { Timber.i(it) }.setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    /**
     * provides an Interceptor object to add general headers
     */
    @Provides
    @Singleton
    fun provideHeaderInterceptor(): Interceptor {
        return Interceptor{
            val requestBuilder = it.request().newBuilder()
            //requestBuilder.addHeader()
            it.proceed(requestBuilder.build())
        }
    }

    /**
     * provides a Http Cache object
     */
    @Provides
    @Singleton
    internal fun provideCache(@ApplicationContext context: Context): Cache {
        val httpCacheDirectory = File(context.cacheDir.absolutePath, "HttpCache")
        return Cache(httpCacheDirectory, CACHE_SIZE_BYTES)
    }

    /**
     * provides a custom OkHTPP object to be used a retrofit client
     * it could be used as a standalone http client
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        headerInterceptor: Interceptor,
        cache: Cache
    ): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
            .readTimeout(READ_TIMEOUT_IN_SECONDS.toLong(), TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT_IN_SECONDS.toLong(), TimeUnit.SECONDS)
            .connectTimeout(CONNECT_TIMEOUT_IN_SECONDS.toLong(), TimeUnit.SECONDS)
            .addInterceptor(headerInterceptor)
            .cache(cache)

        //Enable logging in debug mode only
        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(loggingInterceptor)
        }
        return clientBuilder.build()
    }

    @ExperimentalSerializationApi
    @Provides
    @Singleton
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(httpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun provideServieceApi(retrofit: Retrofit): ServiceApi {
        return retrofit.create(ServiceApi::class.java)
    }

}