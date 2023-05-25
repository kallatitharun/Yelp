package com.example.yelp.data.remoteSource


import com.google.gson.annotations.SerializedName

class ReviewResponse : ArrayList<ReviewResponse.ReviewResponseItem>(){
    data class ReviewResponseItem(
        @SerializedName("date")
        val date: String,
        @SerializedName("rating")
        val rating: Int,
        @SerializedName("text")
        val text: String,
        @SerializedName("username")
        val username: String
    )
}