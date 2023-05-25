package com.example.yelp.presentation.features.businessInformation.reviews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.yelp.domain.base.rvModels.IItemType
import com.example.yelp.domain.navModels.BaseArg
import com.example.yelp.domain.useCases.GetReviewDataUseCase
import com.example.yelp.presentation.base.navigation.BusinessInfoArg
import com.example.yelp.presentation.viewModels.BaseFragmentViewModel

class ReviewsViewModel(
    private val getReviewDataUseCase: GetReviewDataUseCase
): BaseFragmentViewModel() {

    private lateinit var businessId: String

    private val reviewDetailsEvent =  MutableLiveData<List<IItemType>>()
    val reviewDetailsLiveData: LiveData<List<IItemType>> = reviewDetailsEvent

    override fun onForwardArgument(arg: BaseArg) {
        super.onForwardArgument(arg)
        if (arg is BusinessInfoArg) {
            businessId = arg.businessId
            getReviewDetails()
        }
    }

    private fun getReviewDetails() = execute {
        reviewDetailsEvent.value = getReviewDataUseCase.invoke(businessId)
    }
}