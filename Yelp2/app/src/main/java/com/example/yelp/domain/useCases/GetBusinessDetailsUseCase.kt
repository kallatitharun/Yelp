package com.example.yelp.domain.useCases

import com.example.yelp.domain.base.rvModels.IItemType
import com.example.yelp.R

class GetBusinessDetailsUseCase(
    private val yelpService: YelpService
) {
    suspend fun invoke(businessId: String): List<IItemType> {
        val response = yelpService.getBusinessDetails(businessId)
        val completeAddress = response.location.displayAddress.joinToString()
        val newCat = mutableListOf<String>()
        response.categories.forEach {
            newCat.add(it.title)
        }
        val categories = newCat.joinToString(" | ")
        val detailsList = mutableListOf<IItemType>()
        detailsList.add(BusinessDetailsItem(
            address = completeAddress,
            phone = response.displayPhone,
            category = categories,
            priceRange = response.price ?: "$$",
            status = if (response.isClosed)  "Closed" else "Open Now",
            statusColor = if (response.isClosed) R.color.red else R.color.green,
            link = response.url,
            businessName = response.name
        ))

        detailsList.add(
            BusinessDetailsPhotosItem(
                images = response.photos
            )
        )
        return detailsList
    }
}