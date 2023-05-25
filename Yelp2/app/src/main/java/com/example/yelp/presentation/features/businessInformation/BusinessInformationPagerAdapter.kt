package com.example.yelp.presentation.features.businessInformation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.yelp.presentation.base.navigation.BusinessInfoArg
import com.example.yelp.presentation.base.navigation.newFragment
import com.example.yelp.presentation.features.businessInformation.BusinessInformationViewModel.Companion.BUSINESS_DETAILS_TAB_INDEX
import com.example.yelp.presentation.features.businessInformation.BusinessInformationViewModel.Companion.MAP_LOCATION_TAB_INDEX
import com.example.yelp.presentation.features.businessInformation.BusinessInformationViewModel.Companion.REVIEWS_TAB_INDEX
import com.example.yelp.presentation.features.businessInformation.BusinessInformationViewModel.Companion.TOTAL_TABS_IN_SAVED_ITEMS
import com.example.yelp.presentation.features.businessInformation.businessDetails.BusinessDetailsFragment
import com.example.yelp.presentation.features.businessInformation.mapLocation.MapLocationFragment
import com.example.yelp.presentation.features.businessInformation.reviews.ReviewsFragment

class BusinessInformationPagerAdapter(fm: FragmentManager, private val viewModel: BusinessInformationViewModel) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int = TOTAL_TABS_IN_SAVED_ITEMS

    override fun getItem(position: Int): Fragment {
        return when (position) {
            BUSINESS_DETAILS_TAB_INDEX -> newFragment<BusinessDetailsFragment>(BusinessInfoArg(viewModel.getBusinessId(), viewModel.getBusinessName()))
            MAP_LOCATION_TAB_INDEX -> newFragment<MapLocationFragment>(BusinessInfoArg(viewModel.getBusinessId(), viewModel.getBusinessName()))
            REVIEWS_TAB_INDEX -> newFragment<ReviewsFragment>(BusinessInfoArg(viewModel.getBusinessId(), viewModel.getBusinessName()))
            else -> throw IllegalArgumentException("$position doesn't exist")
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            BUSINESS_DETAILS_TAB_INDEX -> "BUSINESS DETAILS"
            MAP_LOCATION_TAB_INDEX -> "MAP LOCATION"
            REVIEWS_TAB_INDEX -> "REVIEWS"
            else -> throw IllegalArgumentException("$position doesn't exist")
        }
    }
}