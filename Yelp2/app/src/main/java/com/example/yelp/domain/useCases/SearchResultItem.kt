package com.example.yelp.domain.useCases

import com.example.yelp.domain.base.rvModels.Diffable
import com.example.yelp.domain.base.rvModels.IItemType

class SearchResultItem(
    val serialNo: Int,
    val businessId: String,
    val imageUrl: String?,
    val businessName: String,
    val rating: String,
    val distance: String,
    val websiteUrl: String
): IItemType, Diffable {
    override fun areItemsTheSame(other: IItemType): Boolean {
        if (other !is SearchResultItem) return false
        return other.businessId == businessId
    }
}