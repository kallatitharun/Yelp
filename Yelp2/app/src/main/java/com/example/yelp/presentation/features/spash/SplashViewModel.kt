package com.example.yelp.presentation.features.spash

import com.example.yelp.presentation.base.navigation.Back
import com.example.yelp.presentation.viewModels.BaseFragmentViewModel
import kotlinx.coroutines.delay

class SplashViewModel: BaseFragmentViewModel() {
    private val minTimeToShowSplash = 1500L

    fun waitAndResume() = execute {
        waitShowingSplash()
        command.value = Back()
    }

    private suspend fun waitShowingSplash() {
        delay(minTimeToShowSplash)
    }
}