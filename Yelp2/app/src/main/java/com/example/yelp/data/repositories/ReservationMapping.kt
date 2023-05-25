package com.example.yelp.data.repositories

import com.example.yelp.data.localSource.db.entities.ReservationsEntity
import com.example.yelp.domain.useCases.Reservations

fun Reservations.mapToData() = ReservationsEntity(
    reservationKey = this.reservationKey,
    date = this.date,
    time = this.time,
    email = this.email,
    business = this.business
)

fun ReservationsEntity.mapToDomain(index: Int) = Reservations(
    reservationKey = this.reservationKey,
    date = this.date,
    time = this.time,
    email = this.email,
    business = this.business,
    countText = index + 1
)