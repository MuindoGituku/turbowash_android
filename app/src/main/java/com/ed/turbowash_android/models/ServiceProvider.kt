package com.ed.turbowash_android.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class ServiceProvider(
    var id:String = "",
    @get:PropertyName("personal_data") @set:PropertyName("personal_data") var personalData: PersonalData,
    @get:PropertyName("banking_info") @set:PropertyName("banking_info") var bankingInfo: BankingInfo,
    @get:PropertyName("home_address") @set:PropertyName("home_address") var homeAddress: SavedAddress,
    @get:PropertyName("preferred_region") @set:PropertyName("preferred_region") var preferredRegion: MapRegion,
    @get:PropertyName("services_offered") @set:PropertyName("services_offered") var servicesOffered: MutableList<String>,
    @get:PropertyName("items_in_possession") @set:PropertyName("items_in_possession") var itemsInPossession: MutableList<String>,
    @get:PropertyName("date_joined") @set:PropertyName("date_joined") var dateJoined: Timestamp,
)

data class BankingInfo(
    @get:PropertyName("payment_nickname") @set:PropertyName("payment_nickname") var paymentNickname: String,
    @get:PropertyName("bank_name") @set:PropertyName("bank_name") var bankName: String,
    @get:PropertyName("name_on_account") @set:PropertyName("name_on_account") var accountName: String,
    @get:PropertyName("account_number") @set:PropertyName("account_number") var accountNumber: String,
    @get:PropertyName("transit_number") @set:PropertyName("transit_number") var transitNumber: String,
    @get:PropertyName("institution_number") @set:PropertyName("institution_number") var institutionNumber: String,
)