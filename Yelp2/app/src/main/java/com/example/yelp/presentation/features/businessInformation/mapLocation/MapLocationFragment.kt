package com.example.yelp.presentation.features.businessInformation.mapLocation

import android.os.Bundle
import android.view.View
import com.example.yelp.databinding.FragmentMapLocationBinding
import com.example.yelp.presentation.base.fragments.BaseFragment
import com.example.yelp.presentation.base.fragments.BinderFactory
import com.example.yelp.presentation.extensions.view.observe
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.androidx.viewmodel.ext.android.viewModel


class MapLocationFragment: BaseFragment<FragmentMapLocationBinding>() {

    override val viewModel: MapLocationViewModel by viewModel()
    override val binderFactory: BinderFactory<FragmentMapLocationBinding>
        get() = FragmentMapLocationBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        withBinder {
            viewLifecycleOwner.observe(viewModel.mapDetailsLiveData) {
                val supportMapFragment: SupportMapFragment = googleMap.getFragment()
                supportMapFragment.getMapAsync { p0 ->
                    val loc = LatLng(it.lat, it.long)
                    p0.addMarker(MarkerOptions().position(loc).title("Marker in loc"))
                    p0.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15F))
                }
            }
        }
    }

}