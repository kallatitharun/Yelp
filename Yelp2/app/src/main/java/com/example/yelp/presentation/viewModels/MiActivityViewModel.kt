package com.example.yelp.presentation.viewModels

import com.example.yelp.presentation.base.navigation.Open
import com.example.yelp.presentation.base.navigation.SplashScreen

class MiActivityViewModel : BaseViewModel() {

    private var isSplashAlreadyShown = false

    fun onExtraScreen(isRestored: Boolean) {
        if (!isRestored) {
            isSplashAlreadyShown = true
            command.value = Open(SplashScreen())
        }
    }

}