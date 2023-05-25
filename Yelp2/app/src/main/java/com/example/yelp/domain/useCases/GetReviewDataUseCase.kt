package com.example.yelp.domain.useCases

import com.example.yelp.domain.base.rvModels.IItemType

class GetReviewDataUseCase(
    private val yelpService: YelpService
) {
    suspend fun invoke(businessId: String): List<IItemType> {
        val response = yelpService.getReviewData(businessId)
        val reviewList = mutableListOf<ReviewItem>()

        response.forEach {
            reviewList.add(
                ReviewItem(
                    autherName = it.username,
                    date = it.date,
                    rating = it.rating.toString(),
                    reviewText = it.text
                )
            )
        }
        return reviewList
    }
}