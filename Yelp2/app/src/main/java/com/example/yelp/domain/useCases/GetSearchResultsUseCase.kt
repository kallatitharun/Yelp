package com.example.yelp.domain.useCases

import com.example.yelp.domain.base.rvModels.IItemType
import kotlin.math.roundToLong

class GetSearchResultsUseCase(
    private val yelpService: YelpService
) {
    suspend fun invoke(
        keyWord: String,
        distance: String,
        category: String,
        lat: String,
        long: String
    ): List<IItemType> {
        val searchResponse = yelpService.getSearchResults(
            keyWord = keyWord,
            distance = distance,
            category = category,
            lat = lat,
            long = long
        )

        val responseList = mutableListOf<SearchResultItem>()

        searchResponse.businesses.forEachIndexed { index, businesse ->

            responseList.add(index,
                SearchResultItem(
                serialNo = index + 1,
                businessId = businesse.id,
                imageUrl = businesse.imageUrl,
                businessName = businesse.name,
                rating = businesse.rating.toString(),
                distance = businesse.distance.roundToLong().toString(), businesse.url
            ))
        }

        return responseList


    }
}