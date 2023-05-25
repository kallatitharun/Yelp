package com.example.yelp.presentation.features.businessInformation

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * A view pager that has a public method for enabling/disabling swipes.
 */
class NoSwipeViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {

    private var pagingEnabled: Boolean = false

    init {
        pagingEnabled = true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (this.pagingEnabled) {
            super.onTouchEvent(event)
        } else false
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (this.pagingEnabled) {
            super.onInterceptTouchEvent(event)
        } else false
    }
}