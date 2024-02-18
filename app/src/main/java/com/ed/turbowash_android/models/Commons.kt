package com.ed.turbowash_android.models

data class PersonalData(
    var fullNames:String,
    var emailAddress:String,
    var phoneNumber:String,
    var profileImage:String,
)

data class SavedAddress(
    var addressTag:String,
    var addressCoordinates: PlaceCoordinates,
    var addressComplete:String,
    var addressCity:String,
    var addressProvince:String,
    var addressCountry:String,
    var addressPostalCode:String,
)

data class PlaceCoordinates(
    var placeLongitude:Double,
    var placeLatitude:Double,
)

data class MapRegion(
    var regionCenter: PlaceCoordinates,
    var regionRadius: Double,
)
