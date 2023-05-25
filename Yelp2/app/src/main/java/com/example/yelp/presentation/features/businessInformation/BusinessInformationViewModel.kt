package com.example.yelp.presentation.features.businessInformation

import com.example.yelp.domain.navModels.BaseArg
import com.example.yelp.presentation.base.navigation.Back
import com.example.yelp.presentation.base.navigation.BusinessInfoArg
import com.example.yelp.presentation.base.navigation.OpenWeb
import com.example.yelp.presentation.viewModels.BaseFragmentViewModel


class BusinessInformationViewModel: BaseFragmentViewModel() {

    private lateinit var businessId: String
    private lateinit var businessName: String
    private lateinit var websiteUrl: String


    override fun onForwardArgument(arg: BaseArg) {
        super.onForwardArgument(arg)
        if (arg is BusinessInfoArg) {
            businessId = arg.businessId
            businessName = arg.businessName
            websiteUrl = arg.websiteUrl

        }
    }

    fun getBusinessId(): String = businessId
    fun getBusinessName() = businessName

    fun onBack() {
        command.value = Back()
    }

    fun faceBookShareClicked() {
        command.value = OpenWeb("https://www.facebook.com/sharer/sharer.php?u=$websiteUrl")
    }

    fun twitterShareClicked() {
        command.value = OpenWeb("https://twitter.com/intent/tweet?text=Check%20$businessName%20on%20Yelp!&url=$websiteUrl")
    }



    companion object {
        const val TOTAL_TABS_IN_SAVED_ITEMS = 3
        const val BUSINESS_DETAILS_TAB_INDEX = 0
        const val MAP_LOCATION_TAB_INDEX = 1
        const val REVIEWS_TAB_INDEX = 2
    }
}