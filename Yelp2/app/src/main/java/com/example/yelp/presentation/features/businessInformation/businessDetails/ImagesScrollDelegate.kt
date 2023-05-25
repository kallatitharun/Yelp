package com.example.yelp.presentation.features.businessInformation.businessDetails

import com.example.yelp.databinding.ItemImagesScrollBinding
import com.example.yelp.presentation.delegate.createDelegate
import com.squareup.picasso.Picasso

fun ImagesScrollDelegate() = createDelegate<ImagesScrollItem, ItemImagesScrollBinding>(ItemImagesScrollBinding::inflate) {
    bind {
        Picasso.get().load(it.url).into(imageItem)
    }
}