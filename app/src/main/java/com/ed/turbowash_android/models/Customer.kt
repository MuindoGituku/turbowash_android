/**
 * @author Group 3 - Muindo Gituku, Emre Deniz, Nkemjika Obi
 * @date Apr, 2024
 */

package com.ed.turbowash_android.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class Customer(
    var id: String = "",
    @get:PropertyName("personal_data") @set:PropertyName("personal_data") var personalData: PersonalData = PersonalData("","","","","","",
        Timestamp.now()),
    @get:PropertyName("saved_payment_cards") @set:PropertyName("saved_payment_cards") var savedPaymentCards: MutableList<PaymentCard> = mutableListOf(),
    @get:PropertyName("saved_addresses") @set:PropertyName("saved_addresses") var savedAddresses: MutableList<SavedAddress> = mutableListOf(),
    @get:PropertyName("saved_vehicles") @set:PropertyName("saved_vehicles") var savedVehicles: MutableList<SavedVehicle> = mutableListOf(),
    @get:PropertyName("favorite_hires") @set:PropertyName("favorite_hires") var favoriteHires: MutableList<String> = mutableListOf(),
    @get:PropertyName("date_joined") @set:PropertyName("date_joined") var dateJoined: Timestamp = Timestamp.now(),
    @get:PropertyName("fcm_token") @set:PropertyName("fcm_token") var fcmToken: String = "",
)

data class SavedVehicle(
    @get:PropertyName("tag") @set:PropertyName("tag") var tag: String = "",
    @get:PropertyName("reg_no") @set:PropertyName("reg_no") var regNo: String = "",
    @get:PropertyName("description") @set:PropertyName("description") var description: String = "",
    @get:PropertyName("images") @set:PropertyName("images") var images: MutableList<String> = mutableListOf(),
)

data class PaymentCard(
    @get:PropertyName("tag") @set:PropertyName("tag") var tag: String = "",
    @get:PropertyName("name_on_card") @set:PropertyName("name_on_card") var nameOnCard: String = "",
    @get:PropertyName("card_number") @set:PropertyName("card_number") var cardNumber: String = "",
    @get:PropertyName("card_cvv") @set:PropertyName("card_cvv") var cardCVV: String = "",
    @get:PropertyName("card_expiry") @set:PropertyName("card_expiry") var cardExpiry: Timestamp = Timestamp.now(),
)
