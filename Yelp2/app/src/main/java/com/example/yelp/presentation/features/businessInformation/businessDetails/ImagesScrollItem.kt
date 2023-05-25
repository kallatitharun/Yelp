package com.example.yelp.presentation.features.businessInformation.businessDetails

import com.example.yelp.domain.base.rvModels.Diffable
import com.example.yelp.domain.base.rvModels.IItemType

class ImagesScrollItem(
    val url: String
): IItemType, Diffable {
    override fun areItemsTheSame(other: IItemType): Boolean {
        if (other !is ImagesScrollItem) return false
        return url == other.url
    }
}