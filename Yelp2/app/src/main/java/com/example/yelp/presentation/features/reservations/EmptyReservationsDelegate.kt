package com.example.yelp.presentation.features.reservations

import com.example.yelp.databinding.ItemEmptyReservationsBinding
import com.example.yelp.domain.useCases.EmptyReservationItem
import com.example.yelp.presentation.delegate.createDelegate

fun EmptyReservationsDelegate() = createDelegate<EmptyReservationItem, ItemEmptyReservationsBinding>(ItemEmptyReservationsBinding::inflate) {

}