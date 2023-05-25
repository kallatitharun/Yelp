package com.example.yelp.presentation.features.businessInformation.reviews

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yelp.databinding.FragmentReviewsBinding
import com.example.yelp.presentation.base.fragments.BaseFragment
import com.example.yelp.presentation.base.fragments.BinderFactory
import com.example.yelp.presentation.extensions.view.observe
import com.example.yelp.presentation.features.businessInformation.BusinessInformationViewModel
import com.example.yelp.presentation.features.businessInformation.businessDetails.BusinessDetailsViewModel
import com.example.yelp.presentation.rvAdapter.ConfigurableAdapter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReviewsFragment: BaseFragment<FragmentReviewsBinding>() {

    override val viewModel: ReviewsViewModel by viewModel()
    override val binderFactory: BinderFactory<FragmentReviewsBinding>
        get() = FragmentReviewsBinding::inflate

    private val adapter by lazy {
        ConfigurableAdapter()
            .addDelegateType(ReviewDelegate())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.observe(viewModel.reviewDetailsLiveData) {
            adapter.replace(it)
        }

        withBinder {
            reviewRecyclerView.layoutManager = LinearLayoutManager(context)
            reviewRecyclerView.adapter = adapter
        }
    }
}