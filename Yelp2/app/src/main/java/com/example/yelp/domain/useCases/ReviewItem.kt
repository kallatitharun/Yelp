package com.example.yelp.domain.useCases

import com.example.yelp.domain.base.rvModels.Diffable
import com.example.yelp.domain.base.rvModels.IItemType

class ReviewItem(
    val autherName: String,
    val date: String,
    val rating: String,
    val reviewText: String
): IItemType, Diffable {
    override fun areItemsTheSame(other: IItemType): Boolean {
        if (other !is ReviewItem) return false
        return reviewText == other.reviewText
    }
}