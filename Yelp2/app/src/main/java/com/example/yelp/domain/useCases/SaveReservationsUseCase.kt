package com.example.yelp.domain.useCases

class SaveReservationsUseCase(
    private val yelpService: YelpService
) {
    suspend fun invoke(reservations: Reservations) {
        val res = mutableListOf<Reservations>()
        res.add(reservations)
        yelpService.saveReservation(res)
    }
}