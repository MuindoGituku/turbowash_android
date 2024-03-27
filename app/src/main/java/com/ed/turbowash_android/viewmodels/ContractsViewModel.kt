package com.ed.turbowash_android.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ed.turbowash_android.exceptions.*
import com.ed.turbowash_android.models.Contract
import com.ed.turbowash_android.models.Customer
import com.ed.turbowash_android.models.PaymentCard
import com.ed.turbowash_android.models.SavedAddress
import com.ed.turbowash_android.models.SavedVehicle
import com.ed.turbowash_android.models.Service
import com.ed.turbowash_android.models.ServiceProvider
import com.ed.turbowash_android.repositories.ContractsRepository
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
class ContractsViewModel @Inject constructor(private val contractsRepository: ContractsRepository) : ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _contractInView = MutableStateFlow<Contract?>(null)
    val contractInView: StateFlow<Contract?> = _contractInView.asStateFlow()

    private val _contractsList = MutableStateFlow<MutableList<Contract>>(mutableListOf())
    val contractsList: StateFlow<MutableList<Contract>> = _contractsList.asStateFlow()

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

    fun initialUploadContractToDB(selectedService: Service, selectedProvider: ServiceProvider, selectedVehicle: SavedVehicle, selectedAddress: SavedAddress, selectedCard: PaymentCard, proposedDate: Date, customerProfile: Customer, washInstructions: String) = launchDataOperation {
        contractsRepository.initialUploadContractToDB(
            selectedService, selectedProvider, selectedVehicle, selectedAddress, selectedCard, proposedDate, customerProfile, washInstructions
        ).also {
            _contractInView.value = it
        }
    }

    fun getContractsListUnderProfile() = launchDataOperation {
        contractsRepository.getContractsListUnderProfile().also { _contractsList.value = it }
    }

    fun sendChatMessageToContract(contractID: String, message: String) = launchDataOperation {
        contractsRepository.sendChatMessageToContract(contractID, message).also { _contractInView.value = it }
    }

    fun rescheduleBookingTime(contractID: String, proposedDate: Date) = launchDataOperation {
        contractsRepository.rescheduleBookingTime(contractID, proposedDate).also { _contractInView.value = it }
    }

    fun cancelCreatedContract(contractID: String, cancelReason: String) = launchDataOperation {
        contractsRepository.cancelCreatedContract(contractID, cancelReason).also { _contractInView.value = it }
    }
}
