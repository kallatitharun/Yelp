package com.example.yelp.presentation.base.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseViewBinderFragment<VB : ViewBinding> : Fragment() {

    var binder: VB? = null
        private set

    abstract val binderFactory: BinderFactory<VB>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        state: Bundle?
    ): View? {
        val newBinder = binderFactory(layoutInflater)
        this.binder = newBinder
        return newBinder.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binder?.onDestroyBinder()
        binder = null
    }

    open fun VB.onDestroyBinder() {}


    fun <R> withBinder(block: VB.() -> R): R? {
        return binder?.block()
    }

}