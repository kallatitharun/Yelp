package com.example.yelp.domain.useCases

import com.example.yelp.domain.base.rvModels.Diffable
import com.example.yelp.domain.base.rvModels.IItemType

class Reservations(
    val reservationKey: String,
    val countText: Int = 1,
    val date: String,
    val time: String,
    val email: String,
    val business: String
): IItemType, Diffable {
    override fun areItemsTheSame(other: IItemType): Boolean {
        if (other !is Reservations) return false
        return reservationKey == other.reservationKey
    }
}