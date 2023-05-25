package com.example.yelp.presentation.features.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.yelp.domain.base.rvModels.IItemType
import com.example.yelp.domain.useCases.GetAutoCompleteUseCase
import com.example.yelp.domain.useCases.GetGeoCodingUseCase
import com.example.yelp.domain.useCases.GetSearchResultsUseCase
import com.example.yelp.domain.useCases.SearchResultItem
import com.example.yelp.presentation.base.navigation.BusinessInfoArg
import com.example.yelp.presentation.base.navigation.BusinessInfoScreen
import com.example.yelp.presentation.base.navigation.Open
import com.example.yelp.presentation.base.navigation.ReservationsScreen
import com.example.yelp.presentation.utils.SingleLiveEvent
import com.example.yelp.presentation.viewModels.BaseFragmentViewModel


class HomeViewModel(
    private val getAutoCompleteUseCase: GetAutoCompleteUseCase,
    private val getSearchResultsUseCase: GetSearchResultsUseCase,
    private val getGeoCodingUseCase: GetGeoCodingUseCase
): BaseFragmentViewModel() {

    private val autoSuggestionResponse = SingleLiveEvent<List<String>>()
    val autoSuggestionsLiveData: LiveData<List<String>> = autoSuggestionResponse

    private val searchResponseEvent = MutableLiveData<List<IItemType>>()
    val searchResponseLiveData: LiveData<List<IItemType>> = searchResponseEvent

    private val locationTextError = SingleLiveEvent<Boolean>()
    val locationTextErrorLiveData: LiveData<Boolean> = locationTextError

    private var isAutoDetectLocationEnabled = false

    private var locationText = ""

    private var category = ""

    private var lat = 0.0
    private var long = 0.0


    fun searchTextChanged(query: String)= execute {
        val response = getAutoCompleteUseCase.invoke(query)
        autoSuggestionResponse.value = response
    }

    fun submitClicked(keyWord: String, distance: String) = execute {
        if (isAutoDetectLocationEnabled) {
            fetchSearchResults(keyWord, distance)
        } else {
            val response = getGeoCodingUseCase.invoke(locationText)
            if (!response.isValid) {
                locationTextError.value = true
            } else {
                lat = response.lat
                long = response.long
                fetchSearchResults(keyWord, distance)
            }
        }
    }

    private fun fetchSearchResults(keyWord: String, distance: String) = execute {

        searchResponseEvent.value = getSearchResultsUseCase.invoke(
            keyWord = keyWord,
            distance = (distance.toLong() * 1600).toString(),
            category = category,
            lat = lat.toString(),
            long = long.toString()
        )
    }

    fun updateCategory(category: String) {
        this.category = category
    }

    fun fetchDeviceLocation() {
        isAutoDetectLocationEnabled = true

    }

    fun autoDetectLocationDisabled() {
        isAutoDetectLocationEnabled = false
    }

    fun locationTextChanced(loc: String) {
        locationText = loc

    }

    fun updateLatLong(lat: Double, long: Double) {
        this.lat = lat
        this.long = long
    }

    fun searchItemClicked(item: SearchResultItem) {
        command.value = Open(BusinessInfoScreen(BusinessInfoArg(item.businessId, item.businessName, item.websiteUrl)))
    }

    fun scheduleButtonCLicked() {
        command.value = Open(ReservationsScreen())
    }
}