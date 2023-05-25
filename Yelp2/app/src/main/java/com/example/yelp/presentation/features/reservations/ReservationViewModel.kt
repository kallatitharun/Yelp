package com.example.yelp.presentation.features.reservations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.yelp.domain.base.rvModels.IItemType
import com.example.yelp.domain.useCases.FetchReservationsUseCase
import com.example.yelp.domain.useCases.RemoveReservationUseCase
import com.example.yelp.domain.useCases.Reservations
import com.example.yelp.presentation.base.navigation.Back
import com.example.yelp.presentation.viewModels.BaseFragmentViewModel

class ReservationViewModel(
    private val fetchReservationsUseCase: FetchReservationsUseCase,
    private val removeReservationUseCase: RemoveReservationUseCase

): BaseFragmentViewModel() {

    private val reservationDetailsEvent =  MutableLiveData<List<IItemType>>()
    val reservationDetailsLiveData: LiveData<List<IItemType>> = reservationDetailsEvent

    init {
        obtainAllReservations()
    }

    private fun obtainAllReservations() = execute {
        reservationDetailsEvent.value = fetchReservationsUseCase.invoke()

    }

    fun deleteReservation(item: Reservations) = execute {
        removeReservationUseCase.invoke(item)
        obtainAllReservations()
    }

    fun onBackPressed() {
        command.value = Back()
    }

}