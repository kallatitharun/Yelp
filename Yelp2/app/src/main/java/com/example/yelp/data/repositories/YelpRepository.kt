package com.example.yelp.data.repositories

import com.example.yelp.data.localSource.db.dao.ReservationsDao
import com.example.yelp.data.remoteSource.*
import com.example.yelp.domain.useCases.Reservations
import com.example.yelp.domain.useCases.YelpService

class YelpRepository(
    private val yelpApi: YelpApi,
    private val reservationsDao: ReservationsDao): YelpService {

    override suspend fun getAutoComplete(query: String): List<String> {
        return yelpApi.getAutoComplete(query)
    }

    override suspend fun getSearchResults(
        keyWord: String,
        distance: String,
        category: String,
        lat: String,
        long: String
    ): SearchResponse {
        return yelpApi.getSearchResults(
            keyWord,
            distance,
            category,
            lat,
            long
        )
    }

    override suspend fun getGeoCoding(loc: String): GeoCodingResponse {
        return yelpApi.getGeoCoding(loc)
    }

    override suspend fun getBusinessDetails(businessId: String): BusinessDetailsResponse {
        return yelpApi.getBusinessDetails(businessId)
    }

    override suspend fun getReviewData(businessId: String): ReviewResponse {
        return yelpApi.getReviewData(businessId)
    }

    override suspend fun removeReservation(item: Reservations) = reservationsDao.remove(item.mapToData())

    override suspend fun saveReservation(reservations: List<Reservations>) = reservationsDao.save(reservations.map { it.mapToData() })

    override suspend fun fetchAllReservations() = reservationsDao.obtainReservations().mapIndexed { index, reservationsEntity -> reservationsEntity.mapToDomain(index) }
}