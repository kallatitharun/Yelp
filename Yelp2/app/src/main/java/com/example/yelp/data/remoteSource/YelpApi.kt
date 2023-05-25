package com.example.yelp.data.remoteSource

import retrofit2.http.GET
import retrofit2.http.Query

interface YelpApi {

    @GET("/api/autoCompleteCall")
    suspend fun getAutoComplete(
        @Query("keyword") query: String
    ): List<String>

    @GET("/api/search")
    suspend fun getSearchResults(
        @Query("keyword") keyWord: String,
        @Query("distance") distance: String,
        @Query("category") category: String,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String
    ): SearchResponse

    @GET("/api/geoCoding")
    suspend fun getGeoCoding(
        @Query("location") loc: String
    ): GeoCodingResponse

    @GET("/api/businessID")
    suspend fun getBusinessDetails(
        @Query("id") businessId: String
    ): BusinessDetailsResponse

    @GET("/api/getReviews")
    suspend fun getReviewData(
        @Query("bizID") businessId: String
    ): ReviewResponse
}