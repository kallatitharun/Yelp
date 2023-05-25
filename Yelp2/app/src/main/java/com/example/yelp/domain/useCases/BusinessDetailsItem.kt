package com.example.yelp.domain.useCases

import androidx.annotation.ColorRes
import com.example.yelp.domain.base.rvModels.Diffable
import com.example.yelp.domain.base.rvModels.IItemType

class BusinessDetailsItem(
    val address: String,
    val phone: String,
    val category: String,
    val priceRange: String,
    val status: String,
    @ColorRes val statusColor: Int,
    val link: String,
    val businessName: String
): IItemType, Diffable {
    override fun areItemsTheSame(other: IItemType): Boolean {
        if (other !is BusinessDetailsItem) return false
        return address == other.address
    }
}