package com.example.yelp.presentation.base.navigation

import android.view.View
import androidx.annotation.AnimRes
import androidx.annotation.StyleRes
import androidx.fragment.app.FragmentTransaction

class NavTransactionOptions private constructor(builder: Builder) {
    val sharedElements: List<Pair<View, String>> = builder.sharedElements
    val transition = builder.transition
    @AnimRes
    val enterAnimation = builder.enterAnimation
    @AnimRes
    val exitAnimation = builder.exitAnimation
    @AnimRes
    val popEnterAnimation = builder.popEnterAnimation
    @AnimRes
    val popExitAnimation = builder.popExitAnimation
    @StyleRes
    val transitionStyle = builder.transitionStyle

    class Builder {
        var sharedElements: MutableList<Pair<View, String>> = mutableListOf()
        var transition: Int = FragmentTransaction.TRANSIT_NONE
        var enterAnimation: Int = 0
        var exitAnimation: Int = 0
        var transitionStyle: Int = 0
        var popEnterAnimation: Int = 0
        var popExitAnimation: Int = 0


        fun build(): NavTransactionOptions {
            return NavTransactionOptions(this)
        }
    }

    companion object {
        fun newBuilder(): Builder {
            return Builder()
        }
    }
}
