package com.example.yelp.presentation.viewModels


import com.example.yelp.domain.navModels.BaseArg
import com.example.yelp.presentation.base.navigation.ToastMessageArg

open class BaseFragmentViewModel : BaseSubFragmentViewModel() {

    open fun onBackwardArgument(arg: BaseArg) {
        if (arg is ToastMessageArg) {
            displayToast(arg.value)
        }
    }

    open fun onForwardArgument(arg: BaseArg) {
    }
}