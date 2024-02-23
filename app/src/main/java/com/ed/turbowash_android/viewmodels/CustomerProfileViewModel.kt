package com.ed.turbowash_android.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ed.turbowash_android.models.Customer
import com.ed.turbowash_android.models.PaymentCard
import com.ed.turbowash_android.models.SavedAddress
import com.ed.turbowash_android.repositories.CustomerProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class CustomerProfileViewModel(private val customerProfileRepo: CustomerProfileRepository) :
    ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _customer = MutableLiveData<Customer>()
    val customer: LiveData<Customer> = _customer

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        loadCustomerProfile()
    }

    private fun loadCustomerProfile() {
        viewModelScope.launch {
            try {
                _loading.value = true

                _customer.value = customerProfileRepo.getCustomerProfile()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun refreshCustomerProfile() = viewModelScope.launch {
        try {
            _loading.value = true

            val profile = customerProfileRepo.getCustomerProfile()
            _customer.value = profile

            _error.value = null
        } catch (e: Exception) {
            _error.value = e.message
        } finally {
            _loading.value = false
        }
    }

    fun initialProfileUpload(
        fullNames: String, phoneNumber: String, gender: String, dateOfBirth: Date, profileImage: Bitmap?,
        homeAddress: String, city: String, province: String, country: String, postalCode: String, longitude: Double, latitude: Double
    ) {
        viewModelScope.launch {
            _error.value = null
            try {
                _loading.value = true

                customerProfileRepo.initialCustomerProfileUpload(
                    fullNames, phoneNumber, gender, dateOfBirth, profileImage,
                    homeAddress, city, province, country, postalCode, longitude, latitude
                )
                loadCustomerProfile()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun updateCustomerPersonalData(
        fullNames: String, phoneNumber: String, bio: String, gender: String, dateOfBirth: Date,
        initialProfileURL: String, profileImage: Bitmap?
    ) {
        viewModelScope.launch {
            _error.value = null
            try {
                _loading.value = true

                customerProfileRepo.updateCustomerPersonalData(
                    fullNames, phoneNumber, bio, gender, dateOfBirth, initialProfileURL, profileImage
                )
                loadCustomerProfile()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun addAddress(newAddress: SavedAddress) {
        viewModelScope.launch {
            _error.value = null
            try {
                _loading.value = true

                customerProfileRepo.addCustomerAddress(newAddress)
                loadCustomerProfile()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun updateAddress(existingAddressTag: String, newAddressDetails: SavedAddress) {
        viewModelScope.launch {
            _error.value = null
            try {
                _loading.value = true

                customerProfileRepo.updateCustomerAddress(existingAddressTag, newAddressDetails)
                loadCustomerProfile()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun deleteAddress(selectedAddressTag: String) {
        viewModelScope.launch {
            _error.value = null
            try {
                _loading.value = true

                customerProfileRepo.deleteCustomerAddress(selectedAddressTag)
                loadCustomerProfile()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun addPaymentCard(newCard: PaymentCard) {
        viewModelScope.launch {
            _error.value = null
            try {
                _loading.value = true

                customerProfileRepo.addCustomerPaymentCard(newCard)
                loadCustomerProfile()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun updatePaymentCard(existingCardTag: String, newCardDetails: PaymentCard) {
        viewModelScope.launch {
            _error.value = null
            try {
                _loading.value = true

                customerProfileRepo.updateCustomerPaymentCard(existingCardTag, newCardDetails)
                loadCustomerProfile()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun deletePaymentCard(selectedCardTag: String) {
        viewModelScope.launch {
            _error.value = null
            try {
                _loading.value = true

                customerProfileRepo.deleteCustomerPaymentCard(selectedCardTag)
                loadCustomerProfile()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
}