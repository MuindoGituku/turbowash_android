package com.ed.turbowash_android.repositories

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePreferencesRepository(@ApplicationContext context: Context): PreferencesRepository {
        return PreferencesRepository(context)
    }
}