package com.example.yelp.domain.useCases

import com.example.yelp.domain.base.rvModels.Diffable
import com.example.yelp.domain.base.rvModels.IItemType

class BusinessDetailsPhotosItem(
    val images: List<String>
): IItemType, Diffable {
    override fun areItemsTheSame(other: IItemType): Boolean {
        if (other !is BusinessDetailsPhotosItem) return false
        return images == other.images
    }
}