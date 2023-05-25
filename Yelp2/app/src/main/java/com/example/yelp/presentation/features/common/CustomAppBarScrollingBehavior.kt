package com.example.yelp.presentation.features.common

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.yelp.presentation.features.businessInformation.OnToolbarScrollListener
import com.google.android.material.appbar.AppBarLayout

class CustomAppBarScrollingBehavior: AppBarLayout.ScrollingViewBehavior {
    constructor() : super()
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    lateinit var scrollListener: OnToolbarScrollListener

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        val onDependentViewChanged = super.onDependentViewChanged(parent, child, dependency)
        scrollListener.onScroll()
        return onDependentViewChanged
    }
}