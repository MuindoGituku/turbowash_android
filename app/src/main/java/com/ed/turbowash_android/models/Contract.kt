/**
 * @author Group 3 - Muindo Gituku, Emre Deniz, Nkemjika Obi
 * @date Apr, 2024
 */

package com.ed.turbowash_android.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class Contract(
    var id: String = "",
    @get:PropertyName("selected_vehicle") @set:PropertyName("selected_vehicle") var vehicle: SavedVehicle = SavedVehicle("","","", mutableListOf()),
    @get:PropertyName("selected_address") @set:PropertyName("selected_address") var address: SavedAddress = SavedAddress("",
        PlaceCoordinates(0.0,0.0),"","","","",""
    ),
    @get:PropertyName("selected_service") @set:PropertyName("selected_service") var service: ContractInfo = ContractInfo("","",0.0,0.0),
    @get:PropertyName("bill_card") @set:PropertyName("bill_card") var billCard: PaymentCard = PaymentCard("","","","", Timestamp.now()),
    @get:PropertyName("contract_title") @set:PropertyName("contract_title") var contractTitle: String = "",
    @get:PropertyName("proposed_date") @set:PropertyName("proposed_date") var proposedDate: Timestamp = Timestamp.now(),
    @get:PropertyName("confirmed_date") @set:PropertyName("confirmed_date") var confirmedDate: Timestamp = Timestamp.now(),
    @get:PropertyName("customer") @set:PropertyName("customer") var customer: BriefProfile = BriefProfile("","",""),
    @get:PropertyName("provider") @set:PropertyName("provider") var provider: BriefProfile = BriefProfile("","",""),
    @get:PropertyName("contract_status") @set:PropertyName("contract_status") var contractStatus: ContractStatus = ContractStatus("","", Timestamp.now(), ""),
    @get:PropertyName("conversation") @set:PropertyName("conversation") var conversation: MutableList<Message> = mutableListOf(),
)

data class Message(
    @get:PropertyName("message") @set:PropertyName("message") var message: String = "",
    @get:PropertyName("send_time") @set:PropertyName("send_time") var sendTime: Timestamp = Timestamp.now(),
    @get:PropertyName("sender_id") @set:PropertyName("sender_id") var senderID: String = "",
)

data class ContractInfo(
    @get:PropertyName("service_id") @set:PropertyName("service_id") var selectedID: String = "",
    @get:PropertyName("wash_instructions") @set:PropertyName("wash_instructions") var washInstructions: String = "",
    @get:PropertyName("provider_charge") @set:PropertyName("provider_charge") var providerCharge: Double = 0.0,
    @get:PropertyName("customer_charge") @set:PropertyName("customer_charge") var customerCharge: Double = 0.0,
)

data class BriefProfile(
    @get:PropertyName("profile_id") @set:PropertyName("profile_id") var profileID: String = "",
    @get:PropertyName("full_names") @set:PropertyName("full_names") var fullNames: String = "",
    @get:PropertyName("image_url") @set:PropertyName("image_url") var imageUrl: String = "",
)

data class ContractStatus(
    @get:PropertyName("desc_message") @set:PropertyName("desc_message") var descriptiveMessage: String = "",
    @get:PropertyName("brief_message") @set:PropertyName("brief_message") var briefMessage: String = "",
    @get:PropertyName("latest_update") @set:PropertyName("latest_update") var latestUpdateTime: Timestamp = Timestamp.now(),
    @get:PropertyName("updater_id") @set:PropertyName("updater_id") var updaterID: String = "",
)