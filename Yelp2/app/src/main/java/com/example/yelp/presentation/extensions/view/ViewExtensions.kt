package com.example.yelp.presentation.extensions.view

import android.app.Activity
import android.content.Context
import android.view.*
import android.view.View.*
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.example.yelp.presentation.base.fragments.ToastMessageEvent

fun View.show() {
    this.visibility = VISIBLE
}

fun View.hide(remainInLayout: Boolean = false) {
    if (remainInLayout) {
        this.visibility = INVISIBLE
    } else {
        this.visibility = GONE
    }
}

fun Context.handleToastMessageEvent(event: ToastMessageEvent) {
    when(event) {
        is ToastMessageEvent.StringResourceMessage -> Toast.makeText(
            this,
            getString(event.message, *event.args.toTypedArray()),
            event.toastLength
        ).show()
        is ToastMessageEvent.StringMessage -> Toast.makeText(
            this,
            event.message,
            event.toastLength
        ).show()
    }

}


fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun <T : Any?> LifecycleOwner.observeIgnoreNull(data: LiveData<T>, onObserve: (T) -> Unit) =
    data.observe(this, { it?.let(onObserve) })

fun <T : Any> LifecycleOwner.observe(data: LiveData<T>, onObserve: (T) -> Unit) =
    data.observe(this, { onObserve(it) })

/**
 * Sets soft[inputMode] for Activity
 */
fun Activity.setSoftInputMode(inputMode: Int) {
    window?.setSoftInputMode(inputMode)
}
