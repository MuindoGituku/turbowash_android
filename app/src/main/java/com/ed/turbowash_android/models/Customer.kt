package com.ed.turbowash_android.models

import java.util.Date

data class Customer(
    var id:String = "",
    var personalData: PersonalData,
    var savedPaymentCards: MutableList<PaymentCard>,
    var savedAddresses: MutableList<SavedAddress>,
    var savedVehicles: MutableList<SavedVehicle>,
    var favoriteHires:MutableList<String>,
)

data class SavedVehicle(
    var vehicleTag:String,
    var vehicleRegNo:String,
    var vehicleDesc:String,
    var vehicleImages:MutableList<String>,
)

data class PaymentCard(
    var cardTag:String,
    var nameOnCard:String,
    var cardNumber:String,
    var cardCVV:String,
    var cardExpriry: Date,
)
