package com.example.yelp.presentation.features.businessInformation.businessDetails

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yelp.databinding.ItemPhotosSlideBinding
import com.example.yelp.domain.useCases.BusinessDetailsPhotosItem
import com.example.yelp.presentation.delegate.createDelegate
import com.example.yelp.presentation.rvAdapter.ConfigurableAdapter

fun ImagesDelegate() = createDelegate<BusinessDetailsPhotosItem, ItemPhotosSlideBinding>(ItemPhotosSlideBinding::inflate) {
    bind {
        val adapter by lazy {
            ConfigurableAdapter()
                .addDelegateType(ImagesScrollDelegate())
        }

        photosRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        photosRecyclerView.adapter = adapter

        val scrollList = mutableListOf<ImagesScrollItem>()
        it.images.forEach { item ->
            scrollList.add(ImagesScrollItem(item))
        }

        adapter.replace(scrollList)

    }
}