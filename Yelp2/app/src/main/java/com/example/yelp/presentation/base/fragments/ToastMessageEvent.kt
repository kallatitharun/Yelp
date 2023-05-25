package com.example.yelp.presentation.base.fragments

import android.widget.Toast
import androidx.annotation.StringRes

sealed class ToastMessageEvent(
    val isShort: Boolean
) {
    val toastLength: Int
        get() = if (isShort) {
            Toast.LENGTH_SHORT
        } else {
            Toast.LENGTH_LONG
        }

    class StringResourceMessage(
        @StringRes val message: Int,
        isShort: Boolean,
        val args: List<Any> = listOf()
    ) : ToastMessageEvent(isShort)

    class StringMessage(
        val message: String,
        isShort: Boolean
    ) : ToastMessageEvent(isShort)

}