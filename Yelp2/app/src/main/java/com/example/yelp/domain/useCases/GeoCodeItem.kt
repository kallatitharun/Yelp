package com.example.yelp.domain.useCases

data class GeoCodeItem(
    val isValid: Boolean,
    val lat: Double,
    val long: Double
)