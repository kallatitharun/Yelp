package com.example.yelp.data.remoteSource


import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("businesses")
    val businesses: List<Businesse>,
    @SerializedName("region")
    val region: Region,
    @SerializedName("total")
    val total: Int
) {
    data class Businesse(
        @SerializedName("alias")
        val alias: String,
        @SerializedName("categories")
        val categories: List<Category>,
        @SerializedName("coordinates")
        val coordinates: Coordinates,
        @SerializedName("display_phone")
        val displayPhone: String,
        @SerializedName("distance")
        val distance: Double,
        @SerializedName("id")
        val id: String,
        @SerializedName("image_url")
        val imageUrl: String?,
        @SerializedName("is_closed")
        val isClosed: Boolean,
        @SerializedName("location")
        val location: Location,
        @SerializedName("name")
        val name: String,
        @SerializedName("phone")
        val phone: String,
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
            @SerializedName("display_address")
            val displayAddress: List<String>,
            @SerializedName("state")
            val state: String,
            @SerializedName("zip_code")
            val zipCode: String
        )
    }

    data class Region(
        @SerializedName("center")
        val center: Center
    ) {
        data class Center(
            @SerializedName("latitude")
            val latitude: Double,
            @SerializedName("longitude")
            val longitude: Double
        )
    }
}