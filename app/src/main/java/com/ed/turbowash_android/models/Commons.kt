package com.ed.turbowash_android.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class PersonalData(
    @get:PropertyName("full_names") @set:PropertyName("full_names") var fullNames: String,
    @get:PropertyName("email_address") @set:PropertyName("email_address") var emailAddress: String,
    @get:PropertyName("phone_number") @set:PropertyName("phone_number") var phoneNumber: String,
    @get:PropertyName("profile_image") @set:PropertyName("profile_image") var profileImage: String,
    @get:PropertyName("bio") @set:PropertyName("bio") var bio: String,
    @get:PropertyName("gender") @set:PropertyName("gender") var gender: String,
    @get:PropertyName("date_of_birth") @set:PropertyName("date_of_birth") var dateOfBirth: Timestamp,
)

data class SavedAddress(
    @get:PropertyName("tag") @set:PropertyName("tag") var tag: String,
    @get:PropertyName("coordinates") @set:PropertyName("coordinates") var coordinates: PlaceCoordinates,
    @get:PropertyName("address") @set:PropertyName("address") var address: String,
    @get:PropertyName("city") @set:PropertyName("city") var city: String,
    @get:PropertyName("province") @set:PropertyName("province") var province: String,
    @get:PropertyName("country") @set:PropertyName("country") var country: String,
    @get:PropertyName("postal_code") @set:PropertyName("postal_code") var postalCode: String,
)

data class PlaceCoordinates(
    @get:PropertyName("longitude") @set:PropertyName("longitude") var longitude: Double,
    @get:PropertyName("latitude") @set:PropertyName("latitude") var latitude: Double,
)

data class MapRegion(
    @get:PropertyName("center") @set:PropertyName("center") var center: PlaceCoordinates,
    @get:PropertyName("radius") @set:PropertyName("radius") var radius: Double,
)

data class Service(
    var id:String = "",
    @get:PropertyName("name") @set:PropertyName("name") var name: String,
    @get:PropertyName("description") @set:PropertyName("description") var description: String,
    @get:PropertyName("thumbnail_url") @set:PropertyName("thumbnail_url") var thumbnailUrl: String,
    @get:PropertyName("recommended_price") @set:PropertyName("recommended_price") var recommendedPrice: Double,
)