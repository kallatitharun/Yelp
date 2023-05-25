package com.example.yelp.domain.useCases

import com.example.yelp.data.remoteSource.BusinessDetailsResponse
import com.example.yelp.data.remoteSource.GeoCodingResponse
import com.example.yelp.data.remoteSource.ReviewResponse
import com.example.yelp.data.remoteSource.SearchResponse

interface YelpService {

    suspend fun getAutoComplete(query: String): List<String>

    suspend fun getSearchResults(keyWord: String,
                                 distance: String,
                                 category: String,
                                 lat: String,
                                 long: String): SearchResponse

    suspend fun getGeoCoding(loc: String): GeoCodingResponse

    suspend fun getBusinessDetails(businessId: String): BusinessDetailsResponse

    suspend fun getReviewData(businessId: String): ReviewResponse

    suspend fun saveReservation(reservations: List<Reservations>)

    suspend fun fetchAllReservations(): List<Reservations>

    suspend fun removeReservation(item: Reservations)



}