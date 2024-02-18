package com.ed.turbowash_android.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.test.core.app.ApplicationProvider
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.tasks.await
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PlaceAddressesViewModel : ViewModel() {
    private val _predictions = MutableLiveData<List<AutocompletePrediction>>()
    val predictions: LiveData<List<AutocompletePrediction>> = _predictions

    private val placesClient: PlacesClient = Places.createClient(ApplicationProvider.getApplicationContext())

    fun searchQuery(query: String) {
        val token = AutocompleteSessionToken.newInstance()
        val request = FindAutocompletePredictionsRequest.builder()
            .setTypeFilter(TypeFilter.ADDRESS)
            .setSessionToken(token)
            .setQuery(query)
            .build()

        viewModelScope.launch {
            val response = placesClient.findAutocompletePredictions(request).await()
            _predictions.value = response.autocompletePredictions
        }
    }

    fun fetchPlaceDetails(placeId: String, fields: List<Place.Field>, onSuccess: (Place) -> Unit) {
        val request = FetchPlaceRequest.newInstance(placeId, fields)
        placesClient.fetchPlace(request).addOnSuccessListener { response ->
            onSuccess(response.place)
        }
    }
}