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

class ContractsRepository() {
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

    suspend fun getIndividualContractInfo(contractID: String): Contract = executeWithExceptionHandling {
        val snapshot = db.collection("contracts").document(contractID).get().await()
        snapshot.toObject(Contract::class.java)?.apply {
            id = snapshot.id
        } ?: throw DataIntegrityException("Profile not found")
    }

    suspend fun getContractsListUnderProfile(): MutableList<Contract> = executeWithExceptionHandling {
        val user = getCurrentUser()

        val collQuery = db.collection("contracts")
            .whereEqualTo("customer.profile_id", user.uid)
            .get().await()

        val contractsList = mutableListOf<Contract>()
        if (collQuery.isEmpty) {
            contractsList
        } else {
            for (document in collQuery.documents) {
                val contract = document.toObject(Contract::class.java)
                contract?.let {
                    it.id = document.id
                    contractsList.add(it)
                }
            }
            contractsList
        }
    }

    suspend fun initialUploadContractToDB(selectedService: Service, selectedProvider: ServiceProvider, selectedVehicle: SavedVehicle, selectedAddress: SavedAddress, selectedCard: PaymentCard, proposedDate: Date, customerProfile: Customer, washInstructions: String): Contract = executeWithExceptionHandling {
        val provider = BriefProfile(selectedProvider.id,selectedProvider.personalData.fullNames,selectedProvider.personalData.profileImage)
        val customer = BriefProfile(customerProfile.id,customerProfile.personalData.fullNames,customerProfile.personalData.profileImage)
        val service = ContractInfo(selectedService.id,washInstructions,selectedService.recommendedPrice,selectedService.recommendedPrice)
        val contractTitle = "${selectedService.name} at ${selectedAddress.tag}"
        val contractStatus = ContractStatus("Wash is currently waiting for confirmation from the service provider. We will notify you when there is an update.","Proposed", Timestamp.now(), customerProfile.id)
        val contract = Contract("",selectedVehicle,selectedAddress,service,selectedCard,contractTitle, Timestamp(proposedDate), Timestamp(proposedDate),customer,provider,contractStatus, mutableListOf())

        db.collection("contracts").add(contract).await()

        contract
    }

    suspend fun sendChatMessageToContract(contractID: String, message: String): Contract = executeWithExceptionHandling {
        val user = getCurrentUser()

        val contractDocRef = db.collection("contracts").document(contractID)

        db.runTransaction { transaction ->
            val contract = transaction.get(contractDocRef).toObject(Contract::class.java)?.apply {
                id = contractID
            } ?: throw IllegalStateException("Contract not found")

            val newMessage = Message(message, Timestamp.now(),user.uid, false)

            contract.conversation.add(newMessage)
            transaction.update(contractDocRef, "conversation", contract.conversation)
        }.await()

        getIndividualContractInfo(contractID)
    }

    suspend fun sendChatAttachmentToContract() = executeWithExceptionHandling {

    }

    suspend fun rescheduleBookingTime(contractID: String, proposedDate: Date): Contract = executeWithExceptionHandling {
        val contractDocRef = db.collection("contracts").document(contractID)

        db.runTransaction { transaction ->
            val contract = transaction.get(contractDocRef).toObject(Contract::class.java)?.apply {
                id = contractID
            } ?: throw IllegalStateException("Contract not found")

            contract.proposedDate = Timestamp(proposedDate)
            transaction.update(contractDocRef, "proposed_date", contract.proposedDate)
        }.await()

        getIndividualContractInfo(contractID)
    }

    suspend fun cancelCreatedContract(contractID: String, cancelReason: String): Contract = executeWithExceptionHandling {
        val user = getCurrentUser()

        val contractDocRef = db.collection("contracts").document(contractID)

        db.runTransaction { transaction ->
            val contract = transaction.get(contractDocRef).toObject(Contract::class.java)?.apply {
                id = contractID
            } ?: throw IllegalStateException("Contract not found")

            contract.contractStatus = ContractStatus("Wash was marked as cancelled. Reason being $cancelReason.","Cancelled", Timestamp.now(), user.uid)
            transaction.update(contractDocRef, "contract_status", contract.contractStatus)
        }.await()

        getIndividualContractInfo(contractID)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object ContractsRepositoryModule {

    @Provides
    @Singleton
    fun provideContractsRepository(): ContractsRepository = ContractsRepository()
}