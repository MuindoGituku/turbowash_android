package com.ed.turbowash_android.models

import com.google.firebase.firestore.PropertyName
import java.util.Date

data class Customer(
    var id: String = "",
    @get:PropertyName("personal_data") @set:PropertyName("personal_data") var personalData: PersonalData,
    @get:PropertyName("saved_payment_cards") @set:PropertyName("saved_payment_cards") var savedPaymentCards: MutableList<PaymentCard>,
    @get:PropertyName("saved_addresses") @set:PropertyName("saved_addresses") var savedAddresses: MutableList<SavedAddress>,
    @get:PropertyName("saved_vehicles") @set:PropertyName("saved_vehicles") var savedVehicles: MutableList<SavedVehicle>,
    @get:PropertyName("favorite_hires") @set:PropertyName("favorite_hires") var favoriteHires: MutableList<String>,
)

data class SavedVehicle(
    @get:PropertyName("vehicle_tag") @set:PropertyName("vehicle_tag") var vehicleTag: String,
    @get:PropertyName("vehicle_reg_no") @set:PropertyName("vehicle_reg_no") var vehicleRegNo: String,
    @get:PropertyName("vehicle_desc") @set:PropertyName("vehicle_desc") var vehicleDesc: String,
    @get:PropertyName("vehicle_images") @set:PropertyName("vehicle_images") var vehicleImages: MutableList<String>,
)

data class PaymentCard(
    @get:PropertyName("card_tag") @set:PropertyName("card_tag") var cardTag: String,
    @get:PropertyName("name_on_card") @set:PropertyName("name_on_card") var nameOnCard: String,
    @get:PropertyName("card_number") @set:PropertyName("card_number") var cardNumber: String,
    @get:PropertyName("card_cvv") @set:PropertyName("card_cvv") var cardCVV: String,
    @get:PropertyName("card_expiry") @set:PropertyName("card_expiry") var cardExpiry: Date,
)
