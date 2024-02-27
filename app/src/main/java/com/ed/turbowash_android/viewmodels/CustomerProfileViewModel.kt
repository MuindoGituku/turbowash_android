package com.ed.turbowash_android.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ed.turbowash_android.exceptions.AuthenticationException
import com.ed.turbowash_android.exceptions.DataIntegrityException
import com.ed.turbowash_android.exceptions.InvalidDataException
import com.ed.turbowash_android.exceptions.NetworkException
import com.ed.turbowash_android.exceptions.OperationFailedException
import com.ed.turbowash_android.exceptions.PermissionDeniedException
import com.ed.turbowash_android.exceptions.QuotaExceededException
import com.ed.turbowash_android.exceptions.ResourceNotFoundException
import com.ed.turbowash_android.exceptions.StorageException
import com.ed.turbowash_android.exceptions.TimeoutException
import com.ed.turbowash_android.models.Customer
import com.ed.turbowash_android.models.PaymentCard
import com.ed.turbowash_android.models.SavedAddress
import com.ed.turbowash_android.repositories.CustomerProfileRepository
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
class CustomerProfileViewModel @Inject constructor(private val customerProfileRepo: CustomerProfileRepository) : ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _customerProfile = MutableStateFlow<Customer?>(null)
    val customerProfile: StateFlow<Customer?> = _customerProfile.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _profileUploadCompleted = MutableStateFlow(false)
    val profileUploadCompleted: StateFlow<Boolean> = _profileUploadCompleted

    init {
        refreshCustomerProfile()
    }

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

    private fun refreshCustomerProfile() = launchDataOperation {
        _customerProfile.value = customerProfileRepo.getCustomerProfile()
    }

    fun resetProfileUploadState() {
        _profileUploadCompleted.value = false
    }

    fun initialProfileUpload(
        fullNames: String, phoneNumber: String, gender: String, dateOfBirth: Date, profileImage: Bitmap?,
        homeAddress: String, city: String, province: String, country: String, postalCode: String, longitude: Double, latitude: Double
    ) = launchDataOperation {
        customerProfileRepo.initialCustomerProfileUpload(
            fullNames, phoneNumber, gender, dateOfBirth, profileImage, homeAddress, city, province, country, postalCode, longitude, latitude
        ).also {
            _customerProfile.value = it
            _profileUploadCompleted.value = true
        }
    }

    fun updateCustomerPersonalData(
        fullNames: String, phoneNumber: String, bio: String, gender: String, dateOfBirth: Date, initialProfileURL: String, profileImage: Bitmap?
    ) = launchDataOperation {
        customerProfileRepo.updateCustomerPersonalData(
            fullNames, phoneNumber, bio, gender, dateOfBirth, initialProfileURL, profileImage
        ).also { _customerProfile.value = it }
    }

    fun addAddress(newAddress: SavedAddress) = launchDataOperation {
        customerProfileRepo.addCustomerAddress(newAddress).also { _customerProfile.value = it }
    }

    fun updateAddress(existingAddressTag: String, newAddressDetails: SavedAddress) = launchDataOperation {
        customerProfileRepo.updateCustomerAddress(existingAddressTag, newAddressDetails).also { _customerProfile.value = it }
    }

    fun deleteAddress(selectedAddressTag: String) = launchDataOperation {
        customerProfileRepo.deleteCustomerAddress(selectedAddressTag).also { _customerProfile.value = it }
    }

    fun addPaymentCard(newCard: PaymentCard) = launchDataOperation {
        customerProfileRepo.addCustomerPaymentCard(newCard).also { _customerProfile.value = it }
    }

    fun updatePaymentCard(existingCardTag: String, newCardDetails: PaymentCard) = launchDataOperation {
        customerProfileRepo.updateCustomerPaymentCard(existingCardTag, newCardDetails).also { _customerProfile.value = it }
    }

    fun deletePaymentCard(selectedCardTag: String) = launchDataOperation {
        customerProfileRepo.deleteCustomerPaymentCard(selectedCardTag).also { _customerProfile.value = it }
    }
}
