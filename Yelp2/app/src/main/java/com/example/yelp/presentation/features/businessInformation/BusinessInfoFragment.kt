package com.example.yelp.presentation.features.businessInformation

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.example.yelp.R
import com.example.yelp.databinding.FragmentBusinessInformationBinding
import com.example.yelp.presentation.base.fragments.BaseFragment
import com.example.yelp.presentation.base.fragments.BinderFactory
import com.example.yelp.presentation.base.navigation.BackHandler
import com.example.yelp.presentation.features.common.FullContentScrollableFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class BusinessInfoFragment : BaseFragment<FragmentBusinessInformationBinding>(),
    FullContentScrollableFragment, BackHandler {

    override val viewModel: BusinessInformationViewModel by sharedViewModel()
    override val binderFactory: BinderFactory<FragmentBusinessInformationBinding> = FragmentBusinessInformationBinding::inflate
    override val activity: Activity?
        get() = this.getActivity()
    override val scrollableView: View?
        get() = binder?.pager
    override val toolbarView: View?
        get() = binder?.appBar

    private lateinit var businessInformationPagerAdapter: BusinessInformationPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initScrollBehaviour(savedInstanceState)
        businessInformationPagerAdapter = BusinessInformationPagerAdapter(childFragmentManager, viewModel)
        withBinder {
            toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            toolbar.setNavigationOnClickListener {
                viewModel.onBack()
            }
            toolbar.inflateMenu(R.menu.share_menu)
            toolbar.setOnMenuItemClickListener {
                if (it.itemId == R.id.facebook_item) {
                    viewModel.faceBookShareClicked()
                } else if (it.itemId == R.id.twitter_item){
                    viewModel.twitterShareClicked()
                }
                return@setOnMenuItemClickListener true
            }
            toolbar.title = viewModel.getBusinessName()
            toolbar.setTitleTextColor(resources.getColor(R.color.white, null))
            pager.adapter = businessInformationPagerAdapter
            tabLayout.setupWithViewPager(pager)
        }

    }

    override fun onBack() {
        viewModel.onBack()
    }

}