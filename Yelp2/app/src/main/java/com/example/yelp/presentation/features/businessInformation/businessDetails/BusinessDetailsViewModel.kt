package com.example.yelp.presentation.features.businessInformation.businessDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.yelp.domain.base.rvModels.IItemType
import com.example.yelp.domain.navModels.BaseArg
import com.example.yelp.domain.useCases.*
import com.example.yelp.presentation.base.navigation.BusinessInfoArg
import com.example.yelp.presentation.viewModels.BaseFragmentViewModel

class BusinessDetailsViewModel(
    private val getBusinessDetailsUseCase: GetBusinessDetailsUseCase,
    private val saveReservationsUseCase: SaveReservationsUseCase
): BaseFragmentViewModel() {

    private lateinit var businessId: String
    private lateinit var businessName: String

    private val businessDetailsEvent =  MutableLiveData<List<IItemType>>()
    val businessDetailsLiveData: LiveData<List<IItemType>> = businessDetailsEvent

    override fun onForwardArgument(arg: BaseArg) {
        super.onForwardArgument(arg)
        if (arg is BusinessInfoArg) {
            businessId = arg.businessId
            businessName = arg.businessName
            getBusinessDetails()
        }
    }

    private fun getBusinessDetails() = execute {
        businessDetailsEvent.value = getBusinessDetailsUseCase.invoke(businessId)
    }

    fun saveReservation(
        email: String,
        date: String,
        time: String
    ) = execute {
        saveReservationsUseCase.invoke(
            Reservations(
                reservationKey = System.currentTimeMillis().toString(),
                date = date,
                time = time,
                email = email,
                business = businessName
            )
        )
    }


}