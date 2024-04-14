/**
 * @author Group 3 - Muindo Gituku, Emre Deniz, Nkemjika Obi
 * @date Apr, 2024
 */

package com.ed.turbowash_android.repositories

import com.ed.turbowash_android.exceptions.*
import com.ed.turbowash_android.models.City
import com.ed.turbowash_android.models.Contract
import com.ed.turbowash_android.models.Schedule
import com.ed.turbowash_android.models.ScheduleLocal
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

class ProviderProfileRepository() {
    private val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private fun getCurrentUser(): FirebaseUser = auth.currentUser ?: throw IllegalStateException("Not logged in")

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

    suspend fun getIndividualProviderProfile(userID: String): ServiceProvider = executeWithExceptionHandling {
        val snapshot = db.collection("providers").document(userID).get().await()
        snapshot.toObject(ServiceProvider::class.java)?.apply {
            id = snapshot.id
        } ?: throw DataIntegrityException("Profile not found")
    }

    suspend fun getIndividualProviderSchedule(userID: String): MutableList<Schedule> = executeWithExceptionHandling {
        val collQuery = db.collection("schedules")
            .whereEqualTo("service_provider", userID)
            .whereGreaterThanOrEqualTo("schedule_day", Timestamp.now())
            .get().await()

        val scheduleList = mutableListOf<Schedule>()
        if (collQuery.isEmpty) {
            scheduleList
        } else {
            for (document in collQuery.documents) {
                val schedule = document.toObject(Schedule::class.java)
                schedule?.let {
                    it.id = document.id
                    scheduleList.add(it)
                }
            }
            scheduleList
        }
    }

    suspend fun getGeneralListOfProvidersFromIDs(providerIDs: MutableList<String>): MutableList<ServiceProvider> = executeWithExceptionHandling {
        val providersList = mutableListOf<ServiceProvider>()
        for (providerID in providerIDs) {
            val provider = getIndividualProviderProfile(providerID)
            providersList.add(provider)
        }
        providersList
    }

    suspend fun getFilteredProvidersWithRegionAndSchedule(selectedWashPeriod: ScheduleLocal, selectedCity: String, selectedProvince: String, selectedServiceID: String): MutableList<ServiceProvider> = executeWithExceptionHandling {
        val schedCollQuery = db.collection("schedules")
            .whereEqualTo("schedule_period.start_time", selectedWashPeriod.schedulePeriod.startTime)
            .whereEqualTo("schedule_period.end_time", selectedWashPeriod.schedulePeriod.endTime)
            .whereEqualTo("schedule_day", selectedWashPeriod.scheduleDay)
            .whereArrayContains("services_offered", selectedServiceID)
            .get().await()

        val providerIDsList = mutableListOf<String>()

        if (schedCollQuery.isEmpty) {
            emptyList<ServiceProvider>()
        } else {
            for (schedDoc in schedCollQuery.documents) {
                val schedule = schedDoc.toObject(Schedule::class.java)
                schedule.let {
                    if (it != null) {
                        it.id = schedDoc.id
                        it.let { it1 ->
                            if (it1.scheduleMapRegion.contains(City(selectedCity, selectedProvince))) {
                                providerIDsList.add(it1.serviceProviderID)
                            }
                        }
                    }
                }
            }
        }
        val providersList = getGeneralListOfProvidersFromIDs(providerIDsList)

        providersList
    }

    suspend fun getPreviousHiredWashersList(): MutableList<ServiceProvider> = executeWithExceptionHandling {
        val user = getCurrentUser()

        val collQuery = db.collection("contracts")
            .whereEqualTo("customer.profile_id", user.uid)
            .get().await()

        val contractsList = mutableListOf<Contract>()
        if (collQuery.isEmpty) {
            mutableListOf()
        } else {
            for (document in collQuery.documents) {
                val contract = document.toObject(Contract::class.java)
                contract?.let {
                    it.id = document.id
                    contractsList.add(it)
                }
            }
            val previousWashersIDsList = mutableListOf<String>()
            contractsList.forEach { contract ->
                previousWashersIDsList.add(contract.provider.profileID)
            }

            val providersList = getGeneralListOfProvidersFromIDs(previousWashersIDsList)

            providersList
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object ProvidersRepositoryModule {

    @Provides
    @Singleton
    fun provideProviderProfileRepository(): ProviderProfileRepository = ProviderProfileRepository()
}