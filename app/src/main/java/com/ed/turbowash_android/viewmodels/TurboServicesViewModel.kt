/**
 * @author Group 3 - Muindo Gituku, Emre Deniz, Nkemjika Obi
 * @date Apr, 2024
 */

package com.ed.turbowash_android.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ed.turbowash_android.exceptions.*
import com.ed.turbowash_android.models.Service
import com.ed.turbowash_android.repositories.TurboServicesRepository
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestoreException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class TurboServicesViewModel @Inject constructor(private val turboServicesRepository: TurboServicesRepository) : ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _servicesList = MutableStateFlow<MutableList<Service>>(mutableListOf())
    val servicesList: StateFlow<MutableList<Service>> = _servicesList.asStateFlow()

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

    fun getAllTurboServices() = launchDataOperation {
        turboServicesRepository.getAllTurboServices().also { _servicesList.value = it }
    }
}
