/**
 * @author Group 3 - Muindo Gituku, Emre Deniz, Nkemjika Obi
 * @date Apr, 2024
 */

package com.ed.turbowash_android.repositories

import com.ed.turbowash_android.exceptions.*
import com.ed.turbowash_android.models.BriefProfile
import com.ed.turbowash_android.models.Contract
import com.ed.turbowash_android.models.ContractInfo
import com.ed.turbowash_android.models.ContractStatus
import com.ed.turbowash_android.models.Customer
import com.ed.turbowash_android.models.Message
import com.ed.turbowash_android.models.PaymentCard
import com.ed.turbowash_android.models.SavedAddress
import com.ed.turbowash_android.models.SavedVehicle
import com.ed.turbowash_android.models.Schedule
import com.ed.turbowash_android.models.Service
import com.ed.turbowash_android.models.ServiceProvider
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.tasks.await
import java.io.IOException
import java.util.Date
import javax.inject.Singleton

class TurboServicesRepository() {
    private val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

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

    suspend fun getAllTurboServices(): MutableList<Service> = executeWithExceptionHandling {

        val collQuery = db.collection("turbowashes")
            .get().await()

        val servicesList = mutableListOf<Service>()
        if (collQuery.isEmpty) {
            servicesList
        } else {
            for (document in collQuery.documents) {
                val service = document.toObject(Service::class.java)
                service?.let {
                    it.id = document.id
                    servicesList.add(it)
                }
            }
            servicesList
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object ServicesRepositoryModule {

    @Provides
    @Singleton
    fun provideTurboServicesRepository(): TurboServicesRepository = TurboServicesRepository()
}