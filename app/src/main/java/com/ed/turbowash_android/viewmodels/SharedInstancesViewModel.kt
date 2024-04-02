package com.ed.turbowash_android.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ed.turbowash_android.models.PaymentCard
import com.ed.turbowash_android.models.SavedAddress
import com.ed.turbowash_android.models.SavedVehicle
import com.ed.turbowash_android.models.Service
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SharedInstancesViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _selectedService = MutableStateFlow<Service?>(null)
    val selectedService = _selectedService.asStateFlow()

    fun updateSelectedService(service: Service){
        _selectedService.value = service
    }

    private val _selectedVehicle = MutableStateFlow<SavedVehicle?>(null)
    val selectedVehicle = _selectedVehicle.asStateFlow()

    fun updateSelectedVehicle(vehicle: SavedVehicle){
        _selectedVehicle.value = vehicle
    }

    private val _selectedAddress = MutableStateFlow<SavedAddress?>(null)
    val selectedAddress = _selectedAddress.asStateFlow()

    fun updateSelectedAddress(address: SavedAddress){
        _selectedAddress.value = address
    }

    private val _selectedPaymentCard = MutableStateFlow<PaymentCard?>(null)
    val selectedPaymentCard = _selectedPaymentCard.asStateFlow()

    fun updateSelectedPaymentCard(card: PaymentCard){
        _selectedPaymentCard.value = card
    }
}