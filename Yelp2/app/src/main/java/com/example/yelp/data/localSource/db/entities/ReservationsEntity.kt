package com.example.yelp.data.localSource.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "reservations"
)
class ReservationsEntity(
    @PrimaryKey
    val reservationKey: String,
    val date: String,
    val time: String,
    val email: String,
    val business: String
) {
}