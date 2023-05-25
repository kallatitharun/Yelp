package com.example.yelp.presentation.features.businessInformation.reviews

import com.example.yelp.databinding.ItemReviewBinding
import com.example.yelp.domain.useCases.ReviewItem
import com.example.yelp.presentation.delegate.createDelegate

fun ReviewDelegate() = createDelegate<ReviewItem, ItemReviewBinding>(ItemReviewBinding::inflate) {
    bind { item->
        autherName.text = item.autherName
        reviewDate.text = item.date.split("\\s".toRegex()).first()
        reviewText.text = item.reviewText
        ratingText.text = "Rating : ${item.rating}/5"
    }
}