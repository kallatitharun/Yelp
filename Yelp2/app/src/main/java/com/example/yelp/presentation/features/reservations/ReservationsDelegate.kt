package com.example.yelp.presentation.features.reservations

import com.example.yelp.databinding.ItemReservationsBinding
import com.example.yelp.domain.useCases.Reservations
import com.example.yelp.presentation.delegate.createDelegate

fun ReservationsDelegate() = createDelegate<Reservations, ItemReservationsBinding>(ItemReservationsBinding::inflate) {
    bind { item->
        countText.text = item.countText.toString()
        businessName.text = item.business
        dateText.text = item.date
        timeText.text = item.time
        emailId.text = item.email
    }
}