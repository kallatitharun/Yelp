package com.example.yelp.domain.useCases

class GetMapLocationDetailsUseCase(
    private val yelpService: YelpService
) {
    suspend fun invoke(businessId: String): MapCoordItem {
        val response = yelpService.getBusinessDetails(businessId)
        return MapCoordItem(
            lat = response.coordinates.latitude,
            long = response.coordinates.longitude
        )
    }
}