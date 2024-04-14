/**
 * @author Group 3 - Muindo Gituku, Emre Deniz, Nkemjika Obi
 * @date Apr, 2024
 */

package com.ed.turbowash_android

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TurboWashApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}