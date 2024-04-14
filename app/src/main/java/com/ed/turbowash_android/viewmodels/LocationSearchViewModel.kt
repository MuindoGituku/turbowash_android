/**
 * @author Group 3 - Muindo Gituku, Emre Deniz, Nkemjika Obi
 * @date Apr, 2024
 */

package com.ed.turbowash_android.viewmodels

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ed.turbowash_android.models.AddressDetails
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class LocationSearchViewModel(application: Application) : AndroidViewModel(application) {
    private val placesClient: PlacesClient = Places.createClient(application)

    private val _searchTerm = MutableStateFlow("")
    val searchTerm = _searchTerm.asStateFlow()

    private val _locationResults = MutableStateFlow<List<AutocompletePrediction>>(emptyList())
    val locationResults = _locationResults.asStateFlow()

    private val _selectedAddressDetails = MutableStateFlow<AddressDetails?>(null)
    val selectedAddressDetails = _selectedAddressDetails.asStateFlow()

    init {
        viewModelScope.launch {
            searchTerm
                .debounce(200)
                .flatMapLatest { query ->
                    searchLocation(query)
                }
                .collect { results ->
                    _locationResults.value = results
                }
        }
    }

    private fun searchLocation(query: String) = flow {
        if (query.isNotEmpty()) {
            val token = AutocompleteSessionToken.newInstance()
            val request = FindAutocompletePredictionsRequest.builder()
                .setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .setQuery(query)
                .build()

            try {
                val autocompletePredictions = placesClient.findAutocompletePredictions(request).await()
                val predictions = autocompletePredictions.autocompletePredictions
                emit(predictions)
            } catch (e: Exception) {
                emit(emptyList<AutocompletePrediction>()) // Handle error appropriately
            }
        } else {
            emit(emptyList<AutocompletePrediction>())
        }
    }

    fun fetchPlaceDetails(placeId: String, context: Context) {
        val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.ADDRESS_COMPONENTS)

        val request = FetchPlaceRequest.newInstance(placeId, placeFields)

        placesClient.fetchPlace(request).addOnSuccessListener { response ->
            val place = response.place
            val addressComponents = place.addressComponents?.asList()
            val city = addressComponents?.firstOrNull { it.types.contains("locality") }?.name ?: ""
            val state = addressComponents?.firstOrNull { it.types.contains("administrative_area_level_1") }?.name ?: ""
            val country = addressComponents?.firstOrNull { it.types.contains("country") }?.name ?: ""
            val postalCode = addressComponents?.firstOrNull { it.types.contains("postal_code") }?.name ?: ""

            _selectedAddressDetails.value = AddressDetails(
                name = place.name ?: "",
                address = place.address ?: "",
                latitude = place.latLng?.latitude ?: 0.0,
                longitude = place.latLng?.longitude ?: 0.0,
                city = city,
                state = state,
                country = country,
                postalCode = postalCode
            )
        }.addOnFailureListener { exception ->
            Toast.makeText(context, "Place not found: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }

    fun updateSearchTerm(query: String) {
        _searchTerm.value = query
    }
}