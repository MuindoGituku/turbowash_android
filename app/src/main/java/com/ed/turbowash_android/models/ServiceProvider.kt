package com.ed.turbowash_android.models

data class ServiceProviderProfile(
    var id:String = "",
    var personalData:PersonalData,
    var bio:String,
    var bankingInfo:PaymentData?,
    var homeAddress:SavedAddress,
    var prefferedRegion: MapRegion?
)

data class PaymentData(
    var paymentNickname:String,
    var bankName:String,
    var accountName:String,
    var accountNumber:String,
    var transitNumber:String,
    var instituionNumber: String,
)