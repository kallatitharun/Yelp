package com.example.yelp.presentation.features.spash

import com.example.yelp.databinding.FragmentSplashBinding
import com.example.yelp.presentation.base.fragments.BaseFragment
import com.example.yelp.presentation.base.fragments.BinderFactory
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment: BaseFragment<FragmentSplashBinding>() {
    override val binderFactory: BinderFactory<FragmentSplashBinding>
        get() = FragmentSplashBinding::inflate

    override val viewModel: SplashViewModel by viewModel()

    override fun onResume() {
        super.onResume()
        viewModel.waitAndResume()
    }
}