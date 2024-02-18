package com.ed.turbowash_android.vewmodelfactories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ed.turbowash_android.viewmodels.UserAuthenticationViewModel

class UserAuthenticationViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserAuthenticationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserAuthenticationViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
