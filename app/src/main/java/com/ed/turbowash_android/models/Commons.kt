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
    @get:PropertyName("address_tag") @set:PropertyName("address_tag") var addressTag: String,
    @get:PropertyName("address_coordinates") @set:PropertyName("address_coordinates") var addressCoordinates: PlaceCoordinates,
    @get:PropertyName("address_complete") @set:PropertyName("address_complete") var addressComplete: String,
    @get:PropertyName("address_city") @set:PropertyName("address_city") var addressCity: String,
    @get:PropertyName("address_province") @set:PropertyName("address_province") var addressProvince: String,
    @get:PropertyName("address_country") @set:PropertyName("address_country") var addressCountry: String,
    @get:PropertyName("address_postal_code") @set:PropertyName("address_postal_code") var addressPostalCode: String,
)

data class PlaceCoordinates(
    @get:PropertyName("place_longitude") @set:PropertyName("place_longitude") var placeLongitude: Double,
    @get:PropertyName("place_latitude") @set:PropertyName("place_latitude") var placeLatitude: Double,
)

data class MapRegion(
    @get:PropertyName("region_center") @set:PropertyName("region_center") var regionCenter: PlaceCoordinates,
    @get:PropertyName("region_radius") @set:PropertyName("region_radius") var regionRadius: Double,
)
