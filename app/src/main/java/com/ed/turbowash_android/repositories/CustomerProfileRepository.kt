package com.ed.turbowash_android.repositories

import android.graphics.Bitmap
import android.util.Log
import com.ed.turbowash_android.models.Customer
import com.ed.turbowash_android.models.PaymentCard
import com.ed.turbowash_android.models.PersonalData
import com.ed.turbowash_android.models.PlaceCoordinates
import com.ed.turbowash_android.models.SavedAddress
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Date

class CustomerProfileRepository {
    private val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val generalDatabaseActionsRepo = GeneralDatabaseActionsRepo()

    suspend fun getCustomerProfile(): Customer {
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("Not logged in")

        return try {
            val snapshot = db.collection("customers").document(userId).get().await()
            snapshot.toObject(Customer::class.java)
                ?: throw IllegalStateException("Profile not found")
        } catch (e: Exception) {
            throw IllegalStateException("Failed to fetch profile: ${e.message}")
        }
    }

    suspend fun initialCustomerProfileUpload(
        fullNames: String,
        phoneNumber: String,
        gender: String,
        dateOfBirth: Date,
        profileImage: Bitmap?,
        homeAddress: String,
        city: String,
        province: String,
        country: String,
        postalCode: String,
        longitude: Double,
        latitude: Double
    ) {
        val user = auth.currentUser ?: throw IllegalStateException("Not logged in")

        var profileImageLink = ""

        if (profileImage != null) {
            try {
                profileImageLink = generalDatabaseActionsRepo.uploadImageToFirebaseStorageAwait(
                    pathString = "profiles/customers/${user.uid}.jpg",
                    bitmap = profileImage
                )
            } catch (e: Exception) {
                throw IllegalStateException("Failed to upload profile image: ${e.message}")
            }
        }

        val personalData = PersonalData(
            fullNames = fullNames,
            emailAddress = user.email ?: "",
            phoneNumber = phoneNumber,
            profileImage = profileImageLink,
            bio = "",
            gender = gender,
            dateOfBirth = Timestamp(dateOfBirth)
        )

        val savedAddress = SavedAddress(
            addressTag = "Home",
            addressCoordinates = PlaceCoordinates(
                placeLongitude = longitude,
                placeLatitude = latitude
            ),
            addressComplete = homeAddress,
            addressCity = city,
            addressProvince = province,
            addressCountry = country,
            addressPostalCode = postalCode
        )

        val customer = Customer(
            personalData = personalData,
            savedPaymentCards = mutableListOf(),
            savedAddresses = mutableListOf(savedAddress),
            savedVehicles = mutableListOf(),
            favoriteHires = mutableListOf()
        )

        db.collection("customers").document(user.uid).set(customer)
            .addOnSuccessListener {
                Log.d("UploadSuccess", "Customer profile successfully uploaded")
            }
            .addOnFailureListener { e ->
                throw IllegalStateException("Failed to upload customer profile: ${e.message}")
            }
    }


    suspend fun updateCustomerPersonalData(
        fullNames: String,
        phoneNumber: String,
        bio: String,
        gender: String,
        dateOfBirth: Date,
        initialProfileURL: String,
        profileImage: Bitmap?
    ) {
        val user = auth.currentUser ?: throw IllegalStateException("Not logged in")

        var profileImageLink = initialProfileURL

        if (profileImage != null) {
            try {
                profileImageLink = generalDatabaseActionsRepo.uploadImageToFirebaseStorageAwait(
                    pathString = "profiles/customers/${user.uid}.jpg",
                    bitmap = profileImage
                )
            } catch (e: Exception) {
                throw IllegalStateException("Failed to upload profile image: ${e.message}")
            }
        }

        val updatedPersonalData = mapOf(
            "personalData.fullNames" to fullNames,
            "personalData.emailAddress" to user.email,
            "personalData.phoneNumber" to phoneNumber,
            "personalData.bio" to bio,
            "personalData.gender" to gender,
            "personalData.dateOfBirth" to Timestamp(dateOfBirth),
            "personalData.profileImage" to profileImageLink
        )

        try {
            db.collection("customers").document(user.uid).update(updatedPersonalData).await()
        } catch (e: Exception) {
            throw IllegalStateException("Failed to update customer data: ${e.message}")
        }
    }

    suspend fun updateCustomerAddress(existingAddressTag: String, newAddressDetails: SavedAddress) {
        val user = auth.currentUser ?: throw IllegalStateException("Not logged in")

        val customerDocRef = db.collection("customers").document(user.uid)

        try {
            db.runTransaction { transaction ->
                val snapshot = transaction.get(customerDocRef)
                val customer = snapshot.toObject(Customer::class.java)

                customer?.savedAddresses?.let { addresses ->
                    val index = addresses.indexOfFirst { it.addressTag == existingAddressTag }
                    if (index != -1) {
                        addresses[index] = newAddressDetails
                        transaction.update(customerDocRef, "savedAddresses", addresses)
                    }
                }
            }.await()
        } catch (e: Exception) {
            throw IllegalStateException("Failed to update address: ${e.message}")
        }
    }

    suspend fun addCustomerAddress(newAddress: SavedAddress) {
        val user = auth.currentUser ?: throw IllegalStateException("Not logged in")

        val customerDocRef = db.collection("customers").document(user.uid)

        try {
            db.runTransaction { transaction ->
                val snapshot = transaction.get(customerDocRef)
                val customer = snapshot.toObject(Customer::class.java)

                customer?.savedAddresses?.add(newAddress)
                transaction.update(customerDocRef, "savedAddresses", customer?.savedAddresses)
            }.await()
        } catch (e: Exception) {
            throw IllegalStateException("Failed to add address: ${e.message}")
        }
    }

    suspend fun deleteCustomerAddress(selectedAddressTag: String) {
        val user = auth.currentUser ?: throw IllegalStateException("Not logged in")

        val customerDocRef = db.collection("customers").document(user.uid)

        try {
            db.runTransaction { transaction ->
                val snapshot = transaction.get(customerDocRef)
                val customer = snapshot.toObject(Customer::class.java)

                customer?.savedAddresses?.let { addresses ->
                    addresses.removeAll { it.addressTag == selectedAddressTag }
                    transaction.update(customerDocRef, "savedAddresses", addresses)
                }
            }.await()
        } catch (e: Exception) {
            throw IllegalStateException("Failed to delete address: ${e.message}")
        }
    }

    suspend fun updateCustomerPaymentCard(existingCardTag: String, newCardDetails: PaymentCard) {
        val user = auth.currentUser ?: throw IllegalStateException("Not logged in")

        val customerDocRef = db.collection("customers").document(user.uid)

        try {
            db.runTransaction { transaction ->
                val snapshot = transaction.get(customerDocRef)
                val customer = snapshot.toObject(Customer::class.java)
                customer?.savedPaymentCards?.let { cards ->
                    val index = cards.indexOfFirst { it.cardTag == existingCardTag }
                    if (index != -1) {
                        cards[index] = newCardDetails
                        transaction.update(customerDocRef, "savedPaymentCards", cards)
                    }
                }
            }.await()
        } catch (e: Exception) {
            throw IllegalStateException("Failed to update payment card: ${e.message}")
        }
    }

    suspend fun addCustomerPaymentCard(newCard: PaymentCard) {
        val user = auth.currentUser ?: throw IllegalStateException("Not logged in")

        val customerDocRef = db.collection("customers").document(user.uid)

        try {
            db.runTransaction { transaction ->
                val snapshot = transaction.get(customerDocRef)
                val customer = snapshot.toObject(Customer::class.java)

                customer?.savedPaymentCards?.add(newCard)
                transaction.update(customerDocRef, "savedPaymentCards", customer?.savedPaymentCards)
            }.await()
        } catch (e: Exception) {
            throw IllegalStateException("Failed to add payment card: ${e.message}")
        }
    }

    suspend fun deleteCustomerPaymentCard(selectedCardTag: String) {
        val user = auth.currentUser ?: throw IllegalStateException("Not logged in")

        val customerDocRef = db.collection("customers").document(user.uid)

        try {
            db.runTransaction { transaction ->
                val snapshot = transaction.get(customerDocRef)
                val customer = snapshot.toObject(Customer::class.java)
                customer?.savedPaymentCards?.let { cards ->
                    cards.removeAll { it.cardTag == selectedCardTag }
                    transaction.update(customerDocRef, "savedPaymentCards", cards)
                }
            }.await()
        } catch (e: Exception) {
            throw IllegalStateException("Failed to delete payment card: ${e.message}")
        }
    }
}