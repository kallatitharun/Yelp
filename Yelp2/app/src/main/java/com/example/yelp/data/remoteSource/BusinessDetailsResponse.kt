package com.example.yelp.data.remoteSource


import com.google.gson.annotations.SerializedName

data class BusinessDetailsResponse(
    @SerializedName("alias")
    val alias: String,
    @SerializedName("categories")
    val categories: List<Category>,
    @SerializedName("coordinates")
    val coordinates: Coordinates,
    @SerializedName("display_phone")
    val displayPhone: String,
    @SerializedName("hours")
    val hours: List<Hour>,
    @SerializedName("id")
    val id: String,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("is_claimed")
    val isClaimed: Boolean,
    @SerializedName("is_closed")
    val isClosed: Boolean,
    @SerializedName("location")
    val location: Location,
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("photos")
    val photos: List<String>,
    @SerializedName("price")
    val price: String?,
    @SerializedName("rating")
    val rating: Double,
    @SerializedName("review_count")
    val reviewCount: Int,
    @SerializedName("transactions")
    val transactions: List<String>,
    @SerializedName("url")
    val url: String
) {
    data class Category(
        @SerializedName("alias")
        val alias: String,
        @SerializedName("title")
        val title: String
    )

    data class Coordinates(
        @SerializedName("latitude")
        val latitude: Double,
        @SerializedName("longitude")
        val longitude: Double
    )

    data class Hour(
        @SerializedName("hours_type")
        val hoursType: String,
        @SerializedName("is_open_now")
        val isOpenNow: Boolean,
        @SerializedName("open")
        val `open`: List<Open>
    ) {
        data class Open(
            @SerializedName("day")
            val day: Int,
            @SerializedName("end")
            val end: String,
            @SerializedName("is_overnight")
            val isOvernight: Boolean,
            @SerializedName("start")
            val start: String
        )
    }

    data class Location(
        @SerializedName("address1")
        val address1: String,
        @SerializedName("address2")
        val address2: String,
        @SerializedName("address3")
        val address3: String,
        @SerializedName("city")
        val city: String,
        @SerializedName("country")
        val country: String,
        @SerializedName("cross_streets")
        val crossStreets: String,
        @SerializedName("display_address")
        val displayAddress: List<String>,
        @SerializedName("state")
        val state: String,
        @SerializedName("zip_code")
        val zipCode: String
    )
}