package com.ed.turbowash_android.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ed.turbowash_android.repositories.PreferencesRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val preferencesRepository: PreferencesRepository) :
    ViewModel() {
    private val _navigationRoute = MutableLiveData<String>()
    val navigationRoute: LiveData<String> = _navigationRoute

    init {
        checkInitialConditions()
    }

    private fun checkInitialConditions() {
        viewModelScope.launch {
            val onboardingComplete = preferencesRepository.checkOnboardingComplete()
            if (!onboardingComplete) {
                _navigationRoute.value = "onboarding"
                return@launch
            }
            val isAuthenticated = checkIfUserIsAuthenticated()
            if (!isAuthenticated) {
                _navigationRoute.value = "auth"
                return@launch
            }
            val userId = getUserId() ?: run {
                _navigationRoute.value = "auth"
                return@launch
            }
            val userIsWasher = checkUserIsWasher(userId)
            if (userIsWasher) {
                _navigationRoute.value = "invalid"
            } else {
                val documentExists = checkFirestoreDocument(userId)
                if (!documentExists) {
                    _navigationRoute.value = "profile"
                } else {
                    _navigationRoute.value = "home"
                }
            }
        }
    }

    private fun checkIfUserIsAuthenticated(): Boolean {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser != null
    }


    private fun getUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    fun onOnboardingComplete() {
        viewModelScope.launch {
            preferencesRepository.setOnboardingComplete()
            _navigationRoute.value = "splash"
            determineNextScreen()
        }
    }

    fun onAuthCompletedSuccessfully() {
        viewModelScope.launch {
            _navigationRoute.value = "splash"
            determineNextScreen()
        }
    }

    fun onProfileCreatedSuccessfully() {
        viewModelScope.launch {
            _navigationRoute.value = "splash"
            determineNextScreen()
        }
    }

    fun onCustomerLogOutSuccessfully() {
        viewModelScope.launch {
            _navigationRoute.value = "splash"
            determineNextScreen()
        }
    }

    fun onContractUploadSuccessfully() {
        _navigationRoute.value = "booking_success"
    }

    fun onNavigateBackHome() {
        viewModelScope.launch {
            _navigationRoute.value = "splash"
            determineNextScreen()
        }
    }

    private suspend fun determineNextScreen() {
        when {
            !checkIfUserIsAuthenticated() -> _navigationRoute.value = "auth"
            getUserId() == null -> _navigationRoute.value = "auth"
            checkUserIsWasher(getUserId()!!) -> _navigationRoute.value = "invalid"
            !checkFirestoreDocument(getUserId()!!) -> _navigationRoute.value = "profile"
            else -> _navigationRoute.value = "home"
        }
    }

    private suspend fun checkFirestoreDocument(userId: String): Boolean {
        return try {
            val db = FirebaseFirestore.getInstance()
            val documentSnapshot = db.collection("customers").document(userId).get().await()
            documentSnapshot.exists()
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun checkUserIsWasher(userId: String): Boolean {
        return try {
            val db = FirebaseFirestore.getInstance()
            val documentSnapshot = db.collection("providers").document(userId).get().await()
            documentSnapshot.exists()
        } catch (e: Exception) {
            false
        }
    }
}
