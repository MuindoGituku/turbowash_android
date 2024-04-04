package com.ed.turbowash_android.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ed.turbowash_android.exceptions.*
import com.ed.turbowash_android.models.Contract
import com.ed.turbowash_android.models.Customer
import com.ed.turbowash_android.models.PaymentCard
import com.ed.turbowash_android.models.SavedAddress
import com.ed.turbowash_android.models.SavedVehicle
import com.ed.turbowash_android.models.Schedule
import com.ed.turbowash_android.models.Service
import com.ed.turbowash_android.models.ServiceProvider
import com.ed.turbowash_android.repositories.ProviderProfileRepository
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestoreException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ProviderProfileViewModel @Inject constructor(private val providerProfileRepo: ProviderProfileRepository) : ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _providerInView = MutableStateFlow<ServiceProvider?>(null)
    val providerInView: StateFlow<ServiceProvider?> = _providerInView.asStateFlow()

    private val _providersList = MutableStateFlow<MutableList<ServiceProvider>>(mutableListOf())
    val providersList: StateFlow<MutableList<ServiceProvider>> = _providersList.asStateFlow()

    private val _providersScheduleList = MutableStateFlow<MutableList<Schedule>>(mutableListOf())
    val providersScheduleList: StateFlow<MutableList<Schedule>> = _providersScheduleList.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private fun launchDataOperation(operation: suspend () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            try {
                operation()
            } catch (e: Exception) {
                _error.value = mapError(e).message
            } finally {
                _loading.value = false
            }
        }
    }

    private fun mapError(e: Exception): Exception {
        return when(e) {
            is FirebaseAuthException -> AuthenticationException("Authentication failed. Please try again.")
            is FirebaseFirestoreException -> DataIntegrityException("Data retrieval error. Please check your connection.")
            is IOException -> NetworkException("Network error. Please check your internet connection.")
            is StorageException -> StorageException("Storage error occurred. Please try again later.")
            is PermissionDeniedException -> PermissionDeniedException("Permission denied. Please ensure the app has the necessary permissions.")
            is ResourceNotFoundException -> ResourceNotFoundException("Requested resource not found.")
            is QuotaExceededException -> QuotaExceededException("Quota exceeded. Please try again later.")
            is InvalidDataException -> InvalidDataException("Invalid data provided.")
            is TimeoutException -> TimeoutException("Request timed out. Please try again.")
            else -> OperationFailedException("An unexpected error occurred: ${e.localizedMessage}. Please try again later.")
        }
    }

    fun getIndividualProviderProfile(userID: String) = launchDataOperation {
        providerProfileRepo.getIndividualProviderProfile(userID).also { _providerInView.value = it }
    }

    fun getIndividualProviderSchedule(userID: String) = launchDataOperation {
        providerProfileRepo.getIndividualProviderSchedule(userID).also { _providersScheduleList.value = it }
    }

    fun getPreviousHiredWashersList() = launchDataOperation {
        providerProfileRepo.getPreviousHiredWashersList().also { _providersList.value = it }
    }

    fun getGeneralListOfProvidersFromIDs(providerIDs: MutableList<String>) = launchDataOperation {
        providerProfileRepo.getGeneralListOfProvidersFromIDs(providerIDs).also { _providersList.value = it }
    }

    fun getFilteredProvidersWithRegionAndSchedule(selectedDate: Date, selectedCity: String, selectedProvince: String, selectedServiceID: String) = launchDataOperation {
        providerProfileRepo.getFilteredProvidersWithRegionAndSchedule(selectedDate, selectedCity, selectedProvince, selectedServiceID).also { _providersList.value = it }
    }
}
