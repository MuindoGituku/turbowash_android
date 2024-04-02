package com.ed.turbowash_android.repositories

import android.graphics.Bitmap
import com.ed.turbowash_android.exceptions.*
import com.ed.turbowash_android.models.Customer
import com.ed.turbowash_android.models.PaymentCard
import com.ed.turbowash_android.models.PersonalData
import com.ed.turbowash_android.models.PlaceCoordinates
import com.ed.turbowash_android.models.SavedAddress
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.tasks.await
import java.io.IOException
import java.util.Date
import javax.inject.Singleton

class CustomerProfileRepository(private val generalDatabaseActionsRepo: GeneralDatabaseActionsRepo) {
    private val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val messaging: FirebaseMessaging by lazy {
        FirebaseMessaging.getInstance()
    }

    private fun getCurrentUser(): FirebaseUser =
        auth.currentUser ?: throw IllegalStateException("Not logged in")

    private suspend fun <T> executeWithExceptionHandling(block: suspend () -> T): T {
        return try {
            block()
        } catch (e: FirebaseFirestoreException) {
            throw DataIntegrityException("Firestore operation failed: ${e.message}")
        } catch (e: FirebaseAuthException) {
            throw AuthenticationException("Auth operation failed: ${e.message}")
        } catch (e: IOException) {
            throw NetworkException("Network error. Please check your internet connection.")
        } catch (e: StorageException) {
            throw StorageException("Storage error occurred. Please try again later.")
        } catch (e: PermissionDeniedException) {
            throw PermissionDeniedException("Permission denied. Please ensure the app has the necessary permissions.")
        } catch (e: ResourceNotFoundException) {
            throw ResourceNotFoundException("Requested resource not found.")
        } catch (e: QuotaExceededException) {
            throw QuotaExceededException("Quota exceeded. Please try again later.")
        } catch (e: InvalidDataException) {
            throw InvalidDataException("Invalid data provided.")
        } catch (e: TimeoutException) {
            throw TimeoutException("Request timed out. Please try again.")
        } catch (e: Exception) {
            throw OperationFailedException("Operation failed: ${e.localizedMessage}. Please try again later.")
        }
    }

    suspend fun getCustomerProfile(): Customer = executeWithExceptionHandling {
        val user = getCurrentUser()
        val currentToken = messaging.token.await()

        val snapshot = db.collection("customers").document(user.uid).get().await()
        snapshot.toObject(Customer::class.java)?.apply {
            id = snapshot.id
            if (fcmToken != currentToken){
                fcmToken = currentToken

                db.collection("customers").document(user.uid).update(mapOf("fcm_token" to currentToken)).await()
            }
        } ?: throw DataIntegrityException("Profile not found")
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
    ): Customer = executeWithExceptionHandling {
        val user = getCurrentUser()
        val currentToken = messaging.token.await()

        var profileImageLink = ""

        if (profileImage != null) {
            profileImageLink = generalDatabaseActionsRepo.uploadImageToFirebaseStorageAwait(
                pathString = "profiles/customers/${user.uid}.jpg",
                bitmap = profileImage
            )
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
            tag = "Home",
            coordinates = PlaceCoordinates(
                longitude = longitude,
                latitude = latitude
            ),
            address = homeAddress,
            city = city,
            province = province,
            country = country,
            postalCode = postalCode
        )

        val customer = Customer(
            personalData = personalData,
            savedPaymentCards = mutableListOf(),
            savedAddresses = mutableListOf(savedAddress),
            savedVehicles = mutableListOf(),
            favoriteHires = mutableListOf(),
            dateJoined = Timestamp.now(),
            fcmToken = currentToken
        )

        db.collection("customers").document(user.uid).set(customer).await()

        customer
    }


    suspend fun updateCustomerPersonalData(
        fullNames: String,
        phoneNumber: String,
        bio: String,
        gender: String,
        dateOfBirth: Date,
        initialProfileURL: String,
        profileImage: Bitmap?
    ): Customer = executeWithExceptionHandling {
        val user = getCurrentUser()

        var profileImageLink = initialProfileURL

        if (profileImage != null) {
            profileImageLink = generalDatabaseActionsRepo.uploadImageToFirebaseStorageAwait(
                pathString = "profiles/customers/${user.uid}.jpg",
                bitmap = profileImage
            )
        }

        val updatedPersonalData = mapOf(
            "personal_data.full_names" to fullNames,
            "personal_data.email_address" to (user.email ?: ""),
            "personal_data.phone_number" to phoneNumber,
            "personal_data.bio" to bio,
            "personal_data.gender" to gender,
            "personal_data.date_of_birth" to Timestamp(dateOfBirth),
            "personal_data.profile_image" to profileImageLink
        )

        db.collection("customers").document(user.uid).update(updatedPersonalData).await()
        getCustomerProfile()
    }


    suspend fun updateCustomerAddress(
        existingAddressTag: String,
        newAddressDetails: SavedAddress
    ): Customer = executeWithExceptionHandling {
        val user = getCurrentUser()

        val customerDocRef = db.collection("customers").document(user.uid)

        db.runTransaction { transaction ->
            val customer = transaction.get(customerDocRef).toObject(Customer::class.java)
                ?: throw IllegalStateException("Customer not found")

            customer.savedAddresses.indexOfFirst { it.tag == existingAddressTag }
                .takeIf { it != -1 }
                ?.let { index ->
                    customer.savedAddresses[index] = newAddressDetails
                    transaction.update(customerDocRef, "saved_addresses", customer.savedAddresses)
                } ?: throw IllegalStateException("Address not found")
        }.await()
        getCustomerProfile()
    }

    suspend fun addCustomerAddress(newAddress: SavedAddress): Customer =
        executeWithExceptionHandling {
            val user = getCurrentUser()

            val customerDocRef = db.collection("customers").document(user.uid)

            db.runTransaction { transaction ->
                val customer = transaction.get(customerDocRef).toObject(Customer::class.java)
                    ?: throw IllegalStateException("Customer not found")

                customer.savedAddresses.add(newAddress)
                transaction.update(customerDocRef, "saved_addresses", customer.savedAddresses)
            }.await()
            getCustomerProfile()
        }

    suspend fun deleteCustomerAddress(selectedAddressTag: String): Customer =
        executeWithExceptionHandling {
            val user = getCurrentUser()

            val customerDocRef = db.collection("customers").document(user.uid)

            db.runTransaction { transaction ->
                val customer = transaction.get(customerDocRef).toObject(Customer::class.java)
                    ?: throw IllegalStateException("Customer not found")

                customer.savedAddresses.removeIf { it.tag == selectedAddressTag }
                transaction.update(customerDocRef, "saved_addresses", customer.savedAddresses)
            }.await()
            getCustomerProfile()
        }

    suspend fun updateCustomerPaymentCard(
        existingCardTag: String,
        newCardDetails: PaymentCard
    ): Customer = executeWithExceptionHandling {
        val user = getCurrentUser()

        val customerDocRef = db.collection("customers").document(user.uid)

        db.runTransaction { transaction ->
            val customer = transaction.get(customerDocRef).toObject(Customer::class.java)
                ?: throw IllegalStateException("Customer not found")

            customer.savedPaymentCards.indexOfFirst { it.tag == existingCardTag }
                .takeIf { it != -1 }
                ?.let { index ->
                    customer.savedPaymentCards[index] = newCardDetails
                    transaction.update(
                        customerDocRef,
                        "saved_payment_cards",
                        customer.savedPaymentCards
                    )
                } ?: throw IllegalStateException("Payment card not found")
        }.await()
        getCustomerProfile()
    }

    suspend fun addCustomerPaymentCard(newCard: PaymentCard): Customer =
        executeWithExceptionHandling {
            val user = getCurrentUser()

            val customerDocRef = db.collection("customers").document(user.uid)

            db.runTransaction { transaction ->
                val customer = transaction.get(customerDocRef).toObject(Customer::class.java)
                    ?: throw IllegalStateException("Customer not found")

                customer.savedPaymentCards.apply {
                    add(newCard)
                    transaction.update(customerDocRef, "saved_payment_cards", this)
                }
            }.await()
            getCustomerProfile()
        }

    suspend fun deleteCustomerPaymentCard(selectedCardTag: String): Customer =
        executeWithExceptionHandling {
            val user = getCurrentUser()

            val customerDocRef = db.collection("customers").document(user.uid)

            db.runTransaction { transaction ->
                val customer = transaction.get(customerDocRef).toObject(Customer::class.java)
                    ?: throw IllegalStateException("Customer not found")

                customer.savedPaymentCards.removeIf { it.tag == selectedCardTag }
                transaction.update(
                    customerDocRef,
                    "saved_payment_cards",
                    customer.savedPaymentCards
                )
            }.await()
            getCustomerProfile()
        }
}

@Module
@InstallIn(SingletonComponent::class)
object CustomerRepositoryModule {

    @Provides
    @Singleton
    fun provideGeneralDatabaseActionsRepo(): GeneralDatabaseActionsRepo =
        GeneralDatabaseActionsRepo()

    @Provides
    @Singleton
    fun provideCustomerProfileRepository(
        generalDatabaseActionsRepo: GeneralDatabaseActionsRepo
    ): CustomerProfileRepository = CustomerProfileRepository(generalDatabaseActionsRepo)
}