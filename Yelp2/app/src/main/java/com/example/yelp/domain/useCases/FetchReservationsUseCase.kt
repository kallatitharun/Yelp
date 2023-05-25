package com.example.yelp.domain.useCases

import com.example.yelp.domain.base.rvModels.IItemType

class FetchReservationsUseCase(
    private val yelpService: YelpService
) {
    suspend fun invoke(): List<IItemType> {
        val resposnse = yelpService.fetchAllReservations()
        return if (resposnse.isEmpty()) {
            mutableListOf(EmptyReservationItem())
        } else {
            resposnse
        }
    }
}