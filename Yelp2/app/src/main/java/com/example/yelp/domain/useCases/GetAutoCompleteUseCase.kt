package com.example.yelp.domain.useCases

class GetAutoCompleteUseCase(private val yelpService: YelpService) {

    suspend fun invoke(query: String): List<String> {
        return yelpService.getAutoComplete(query)
    }

}