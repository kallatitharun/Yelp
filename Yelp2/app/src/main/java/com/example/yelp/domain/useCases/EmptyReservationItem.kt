package com.example.yelp.domain.useCases

import com.example.yelp.domain.base.rvModels.Diffable
import com.example.yelp.domain.base.rvModels.IItemType

class EmptyReservationItem : IItemType, Diffable {
    override fun areItemsTheSame(other: IItemType): Boolean {
        if (other !is EmptyReservationItem) return false
        return true
    }
}