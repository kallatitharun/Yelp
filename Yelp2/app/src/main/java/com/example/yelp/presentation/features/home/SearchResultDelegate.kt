package com.example.yelp.presentation.features.home

import com.example.yelp.databinding.ItemSearchResultBinding
import com.example.yelp.domain.useCases.SearchResultItem
import com.example.yelp.presentation.delegate.createDelegate
import com.squareup.picasso.Picasso

fun SearchResultDelegate(onItemClick: (SearchResultItem) -> Unit) = createDelegate<SearchResultItem, ItemSearchResultBinding>(ItemSearchResultBinding::inflate) {

    bind {
        countText.text = it.serialNo.toString()
        name.text = it.businessName
        rating.text = it.rating
        distance.text = (it.distance.toLong()/1600).toString()
        if (!it.imageUrl.isNullOrBlank()) {
            Picasso.get().load(it.imageUrl).into(image)
        }
    }

    listeners { provider ->
        root.setOnClickListener {
            provider()?.let { item ->
                onItemClick(item)
            }
        }
    }
}