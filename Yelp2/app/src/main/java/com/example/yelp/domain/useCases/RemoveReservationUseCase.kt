package com.example.yelp.domain.useCases

class RemoveReservationUseCase(
    private val yelpService: YelpService
) {
    suspend fun invoke(item: Reservations) {
        yelpService.removeReservation(item)
    }
}