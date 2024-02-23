package com.ed.turbowash_android.models

data class ServiceProviderProfile(
    var id:String = "",
    var personalData:PersonalData,
    var bankingInfo:PaymentData?,
    var homeAddress:SavedAddress,
    var prefferedRegion: MapRegion?,
    var servicesOffered:MutableList<String>,
    var itemsInPossesion:MutableList<String>,
)

data class PaymentData(
    var paymentNickname:String,
    var bankName:String,
    var accountName:String,
    var accountNumber:String,
    var transitNumber:String,
    var instituionNumber: String,
)