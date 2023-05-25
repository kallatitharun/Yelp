package com.example.yelp.presentation.features.businessInformation.mapLocation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.yelp.domain.navModels.BaseArg
import com.example.yelp.domain.useCases.GetMapLocationDetailsUseCase
import com.example.yelp.domain.useCases.MapCoordItem
import com.example.yelp.presentation.base.navigation.BusinessInfoArg
import com.example.yelp.presentation.viewModels.BaseFragmentViewModel

class MapLocationViewModel(
    private val getMapLocationDetailsUseCase: GetMapLocationDetailsUseCase
): BaseFragmentViewModel() {

    private lateinit var businessId: String

    private val mapDetailsEvent =  MutableLiveData<MapCoordItem>()
    val mapDetailsLiveData: LiveData<MapCoordItem> = mapDetailsEvent

    override fun onForwardArgument(arg: BaseArg) {
        super.onForwardArgument(arg)
        if (arg is BusinessInfoArg) {
            businessId = arg.businessId
            getBusinessDetails()
        }
    }

    private fun getBusinessDetails() = execute {
        mapDetailsEvent.value = getMapLocationDetailsUseCase.invoke(businessId)
    }

}