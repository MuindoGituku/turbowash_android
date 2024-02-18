package com.ed.turbowash_android.repositories

import android.content.Context

class PreferencesRepository(private val context: Context) {

    fun checkOnboardingComplete(): Boolean {
        val sharedPreferences = context.getSharedPreferences("TurboWash", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("OnboardingComplete", false)
    }

    fun setOnboardingComplete() {
        val sharedPreferences = context.getSharedPreferences("TurboWash", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("OnboardingComplete", true)
            apply()
        }
    }
}