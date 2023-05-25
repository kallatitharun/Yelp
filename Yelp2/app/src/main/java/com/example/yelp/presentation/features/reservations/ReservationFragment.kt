package com.example.yelp.presentation.features.reservations

import android.os.Bundle
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yelp.databinding.FragmentReservationBinding
import com.example.yelp.domain.useCases.Reservations
import com.example.yelp.presentation.base.fragments.BaseFragment
import com.example.yelp.presentation.base.fragments.BinderFactory
import com.example.yelp.presentation.base.navigation.BackHandler
import com.example.yelp.presentation.extensions.view.observe
import com.example.yelp.presentation.rvAdapter.ConfigurableAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReservationFragment: BaseFragment<FragmentReservationBinding>(), BackHandler {
    override val binderFactory: BinderFactory<FragmentReservationBinding>
        get() = FragmentReservationBinding::inflate

    override val viewModel: ReservationViewModel by viewModel()

    private val adapter by lazy {
        ConfigurableAdapter()
            .addDelegateType(ReservationsDelegate())
            .addDelegateType(EmptyReservationsDelegate())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.observe(viewModel.reservationDetailsLiveData) {
            adapter.replace(it)
        }

        withBinder {
            reservationsRecyclerView.layoutManager = LinearLayoutManager(context)
            reservationsRecyclerView.adapter = adapter

            backButton.setOnClickListener {
                viewModel.onBackPressed()
            }


            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                    val position = viewHolder.adapterPosition

                    viewModel.deleteReservation(adapter.getItem(position) as Reservations)
                    adapter.removeItem(adapter.getItem(position))

                    adapter.notifyItemRemoved(viewHolder.adapterPosition)

                }

            }).attachToRecyclerView(reservationsRecyclerView)

        }
    }

    override fun onBack() {
        viewModel.onBackPressed()
    }
}