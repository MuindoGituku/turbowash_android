package com.ed.turbowash_android.models

data class AddressDetails(
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val city: String,
    val state: String,
    val country: String,
    val postalCode: String
)
