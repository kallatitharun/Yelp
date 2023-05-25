package com.example.yelp.domain.useCases

class GetGeoCodingUseCase(
    private val yelpService: YelpService
) {
    suspend fun invoke(loc: String): GeoCodeItem {
        val geoCodeResponse = yelpService.getGeoCoding(loc)
        return if (geoCodeResponse.results.isEmpty()) {
            GeoCodeItem(
                isValid = false,
                lat = 0.0,
                long = 0.0
            )
        } else {
            GeoCodeItem(
                isValid = true,
                lat = geoCodeResponse.results[0].geometry.location.lat,
                long = geoCodeResponse.results[0].geometry.location.lng
            )
        }
    }
}