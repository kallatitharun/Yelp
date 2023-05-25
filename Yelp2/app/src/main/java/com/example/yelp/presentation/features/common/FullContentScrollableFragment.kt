package com.example.yelp.presentation.features.common

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.yelp.presentation.base.activities.ScrollHandler
import com.example.yelp.presentation.features.businessInformation.OnToolbarScrollListener

interface FullContentScrollableFragment {

    val activity: Activity?
    val scrollableView: View?
    val toolbarView: View?

    /**
     * Should be invoked in onViewCreated to initialized scrollable behaviour
     */
    fun initScrollBehaviour(savedInstanceState: Bundle?) {
        val scrollHandler = activity as? ScrollHandler
        if (savedInstanceState == null) {
            resetScroll()
        }

        val scollableContent = scrollableView ?: return
        val toolbar = toolbarView ?: return

        val layoutParams = scollableContent.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = layoutParams.behavior as CustomAppBarScrollingBehavior
        behavior.scrollListener = object : OnToolbarScrollListener {
            override fun onScroll() {
                val deltaScrollY = toolbar.y.toInt()
                scrollHandler?.onScroll(deltaScrollY)
            }
        }
    }

    fun resetScroll() {
        (activity as? ScrollHandler)?.onScroll(0)
    }

}