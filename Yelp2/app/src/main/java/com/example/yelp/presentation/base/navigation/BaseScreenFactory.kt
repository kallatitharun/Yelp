package com.example.yelp.presentation.base.navigation

import androidx.fragment.app.Fragment
import com.example.yelp.domain.exceptions.NoScreenFoundException
import com.example.yelp.presentation.features.businessInformation.BusinessInfoFragment
import com.example.yelp.presentation.features.home.HomeFragment
import com.example.yelp.presentation.features.reservations.ReservationFragment
import com.example.yelp.presentation.features.spash.SplashFragment

/**
 * Base screen factory that create instances of fragments based on given screen name
 */
open class BaseScreenFactory {

    /**
     * Create instances of fragments based on given screen name
     * @param screen to get fragment
     */
    open fun getScreen(screen: Screen): Fragment {
        return when (screen) {
            is HomeScreen -> newFragment<HomeFragment>()
            is BusinessInfoScreen -> newFragment<BusinessInfoFragment>(screen.param)
            is ReservationsScreen -> newFragment<ReservationFragment>()
            is SplashScreen -> newFragment<SplashFragment>()
            else -> throw NoScreenFoundException()
        }
    }

}
