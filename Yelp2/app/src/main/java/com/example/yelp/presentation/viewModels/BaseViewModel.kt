package com.example.yelp.presentation.viewModels

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yelp.presentation.base.fragments.ToastMessageEvent
import com.example.yelp.presentation.base.navigation.Command
import com.example.yelp.presentation.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import timber.log.Timber

open class BaseViewModel : ViewModel() {

    val command = SingleLiveEvent<Command>()

    private val toastMessageData = SingleLiveEvent<ToastMessageEvent>()
    val toastMessage: LiveData<ToastMessageEvent> = toastMessageData

    fun displayToast(@StringRes message: Int, isShort: Boolean = true) {
        toastMessageData.value = ToastMessageEvent.StringResourceMessage(message, isShort)
    }

    fun displayToast(message: String, isShort: Boolean = true) {
        toastMessageData.value = ToastMessageEvent.StringMessage(message, isShort)
    }

    /**
     * Serves to run coroutine and handle errors
     */
    protected fun execute(action: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                action()
            } catch (e: Throwable) {
                onError(e)
            }
        }
    }

    /**
     * Callback helps to handle all exceptions that may appear during executing action.
     * Override it in viewmodel to handle.
     */
    private fun onError(e: Throwable) {
        Timber.e("Received Exception in BaseViewModel: $e")
    }
}