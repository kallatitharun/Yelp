package com.example.yelp.presentation.base.fragments

import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import com.example.yelp.domain.navModels.BaseArg
import com.example.yelp.presentation.base.navigation.ArgumentType
import com.example.yelp.presentation.base.navigation.argValue
import com.example.yelp.presentation.base.navigation.obtainArgAndClear
import com.example.yelp.presentation.viewModels.BaseFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Contains basic logic for all Fragments
 */
abstract class BaseFragment<VB: ViewBinding> : BaseSubFragment<VB>() {

    override val viewModel: BaseFragmentViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        obtainArgAndClear<BaseArg>(ArgumentType.BACKWARD)?.let {
            viewModel.onBackwardArgument(it)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        argValue<BaseArg>(ArgumentType.FORWARD)?.let {
            viewModel.onForwardArgument(it)
        }
    }

    override fun onDetach() {
        super.onDetach()
        navigator = null
    }

}