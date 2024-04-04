package com.ed.turbowash_android.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ed.turbowash_android.models.PaymentCard
import com.ed.turbowash_android.models.SavedAddress
import com.ed.turbowash_android.models.SavedVehicle
import com.ed.turbowash_android.models.Service
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SharedInstancesViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    @RequiresApi(Build.VERSION_CODES.O)
    private val _selectedWashDate = MutableStateFlow<LocalDate>(LocalDate.now())
    @RequiresApi(Build.VERSION_CODES.O)
    val selectedWashDate = _selectedWashDate.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateSelectedWashDate(date: LocalDate){
        _selectedWashDate.value = date
    }

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

    private val _washInstructions = MutableStateFlow("")
    val washInstructions = _washInstructions.asStateFlow()

    fun updateWashInstructions(instructions: String){
        _washInstructions.value = instructions
    }

    private val _selectedContractID = MutableStateFlow("")
    val selectedContractID = _selectedContractID.asStateFlow()

    fun updateSelectedContractID(id: String){
        _selectedContractID.value = id
    }

    private val _selectedProviderID = MutableStateFlow("")
    val selectedProviderID = _selectedProviderID.asStateFlow()

    fun updateSelectedProviderID(id: String){
        _selectedProviderID.value = id
    }
}