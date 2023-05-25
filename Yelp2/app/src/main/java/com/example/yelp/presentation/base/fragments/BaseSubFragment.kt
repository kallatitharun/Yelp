package com.example.yelp.presentation.base.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.CallSuper
import androidx.viewbinding.ViewBinding
import com.example.yelp.presentation.base.navigation.INavigator
import com.example.yelp.presentation.extensions.view.handleToastMessageEvent
import com.example.yelp.presentation.extensions.view.setSoftInputMode
import com.example.yelp.presentation.viewModels.BaseSubFragmentViewModel

typealias BinderFactory<VB> = (LayoutInflater) -> VB
/**
 * BaseFragment is used for fragments which are being added to root activity.
 * We need control over bottom bar visibility, or handling backward and forward arguments,
 * or showing no-wifi message.But also we have fragment inside other fragments and
 * we do not need such actions and do not want to affect parent fragment.
 * So that's why we need separate class for SubFragments
 */
abstract class BaseSubFragment<VB : ViewBinding> : BaseViewBinderFragment<VB>() {

    abstract val viewModel: BaseSubFragmentViewModel

    // Navigator that let use navigation
    var navigator: INavigator? = null
    private var originalSoftInputMode: Int? = null


    override fun onDestroy() {
        super.onDestroy()

        originalSoftInputMode?.let { activity?.setSoftInputMode(it) }
    }

    @CallSuper
    override fun onViewCreated(view: View,savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.toastMessage.observe(viewLifecycleOwner) {
            context?.handleToastMessageEvent(it)
        }

        // Execute given navigation command
        viewModel.command.observe(viewLifecycleOwner) {
            it?.let {
                navigator?.execute(it)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is INavigator) {
            navigator = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        navigator = null
    }

}