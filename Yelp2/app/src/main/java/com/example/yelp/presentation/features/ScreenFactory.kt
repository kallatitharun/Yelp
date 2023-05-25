package com.example.yelp.presentation.features

import androidx.fragment.app.Fragment
import com.example.yelp.presentation.base.navigation.BaseScreenFactory
import com.example.yelp.presentation.base.navigation.HomeScreen
import com.example.yelp.presentation.base.navigation.Screen
import com.example.yelp.presentation.base.navigation.newFragment
import com.example.yelp.presentation.features.home.HomeFragment

/**
 * Mi screen factory that creates instances of fragments based on given screen name
 */
class ScreenFactory : BaseScreenFactory() {

    override fun getScreen(screen: Screen): Fragment {
        return when (screen) {
            is HomeScreen -> newFragment<HomeFragment>()
            else -> return super.getScreen(screen)
        }
    }
}