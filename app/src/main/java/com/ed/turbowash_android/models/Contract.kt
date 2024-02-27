package com.ed.turbowash_android.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class Contract(
    var id: String = "",
    @get:PropertyName("selected_vehicle") @set:PropertyName("selected_vehicle") var vehicle: SavedVehicle,
    @get:PropertyName("selected_address") @set:PropertyName("selected_address") var address: SavedAddress,
    @get:PropertyName("selected_service") @set:PropertyName("selected_service") var service: ContractInfo,
    @get:PropertyName("bill_card") @set:PropertyName("bill_card") var billCard: PaymentCard,
    @get:PropertyName("proposed_date") @set:PropertyName("proposed_date") var proposedDate: Timestamp,
    @get:PropertyName("confirmed_date") @set:PropertyName("confirmed_date") var confirmedDate: Timestamp,
    @get:PropertyName("customer") @set:PropertyName("customer") var customer: BriefProfile,
    @get:PropertyName("provider") @set:PropertyName("provider") var provider: BriefProfile,
    @get:PropertyName("contract_status") @set:PropertyName("contract_status") var contractStatus: String,
    @get:PropertyName("conversation") @set:PropertyName("conversation") var conversation: MutableList<Message>,
)

data class Message(
    @get:PropertyName("message") @set:PropertyName("message") var message: String,
    @get:PropertyName("send_time") @set:PropertyName("send_time") var sendTime: Timestamp,
    @get:PropertyName("sender_id") @set:PropertyName("sender_id") var senderID: String,
)

data class ContractInfo(
    @get:PropertyName("service_id") @set:PropertyName("service_id") var selectedID: String,
    @get:PropertyName("wash_instructions") @set:PropertyName("wash_instructions") var washInstructions: String,
    @get:PropertyName("provider_charge") @set:PropertyName("provider_charge") var providerCharge: Double,
    @get:PropertyName("customer_charge") @set:PropertyName("customer_charge") var customerCharge: Double,
)

data class BriefProfile(
    @get:PropertyName("profile_id") @set:PropertyName("profile_id") var message: String,
    @get:PropertyName("full_names") @set:PropertyName("full_names") var sendTime: String,
    @get:PropertyName("image_url") @set:PropertyName("image_url") var senderID: String,
)