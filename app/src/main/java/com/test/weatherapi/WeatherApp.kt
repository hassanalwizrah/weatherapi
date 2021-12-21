package com.test.weatherapi

import android.app.Application
import coil.Coil
import coil.ImageLoader
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class WeatherApp: Application() {

    @Inject
    lateinit var coilLoader: ImageLoader

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        Coil.setImageLoader(coilLoader)
    }

}