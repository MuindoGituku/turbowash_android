package com.ed.turbowash_android

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TurboWashApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}