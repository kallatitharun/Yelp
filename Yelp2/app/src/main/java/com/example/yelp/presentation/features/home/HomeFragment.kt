package com.example.yelp.presentation.features.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yelp.databinding.FragmentHomeBinding
import com.example.yelp.presentation.base.fragments.BaseFragment
import com.example.yelp.presentation.base.fragments.BinderFactory
import com.example.yelp.presentation.extensions.view.hide
import com.example.yelp.presentation.extensions.view.observe
import com.example.yelp.presentation.extensions.view.show
import com.example.yelp.presentation.rvAdapter.ConfigurableAdapter
import com.example.yelp.presentation.utils.EmptyTextWatcher
import com.google.android.gms.location.LocationServices
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment: BaseFragment<FragmentHomeBinding>() {

    override val viewModel: HomeViewModel by viewModel()

    override val binderFactory: BinderFactory<FragmentHomeBinding> =
        FragmentHomeBinding::inflate

    private val listAdapter by lazy {
        ConfigurableAdapter()
            .addDelegateType(SearchResultDelegate(viewModel::searchItemClicked))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryMap = HashMap<String, String>()

        categoryMap["Default"] = "all"
        categoryMap["Arts & Entertainment"] = "arts"
        categoryMap["Health & Medical"] = "health"
        categoryMap["Hotels & Travel"] = "hotelstravel"
        categoryMap["Food"] = "food"
        categoryMap["Professional Services"] = "professional"

        val categoryList = mutableListOf<String>()
        categoryList.add("Default")
        categoryList.add("Arts & Entertainment")
        categoryList.add("Health & Medical")
        categoryList.add("Hotels & Travel")
        categoryList.add("Food")
        categoryList.add("Professional Services")

        viewLifecycleOwner.observe(viewModel.autoSuggestionsLiveData) {
            withBinder {
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, it)
                keyWordTextField.setAdapter(adapter)
            }
        }

        viewLifecycleOwner.observe(viewModel.searchResponseLiveData) {
            listAdapter.replace(it)
        }

        viewLifecycleOwner.observe(viewModel.locationTextErrorLiveData) {
            withBinder {
                locationTextField.setText("")
                locationTextField.error = "invalid location"
            }
        }

        withBinder {

            scheduleButton.setOnClickListener {
                viewModel.scheduleButtonCLicked()
            }

            clearButton.setOnClickListener {
                keyWordTextField.setText("")
                distanceTextField.setText("")
                locationTextField.setText("")
                autoDetectCheckBox.isChecked = false
                listAdapter.clear()
            }

            keyWordTextField.addTextChangedListener(object : EmptyTextWatcher() {
                override fun afterTextChanged(s: Editable) {
                    viewModel.searchTextChanged(s.toString())
                }
            })

            locationTextField.addTextChangedListener(object : EmptyTextWatcher() {
                override fun afterTextChanged(s: Editable) {
                    viewModel.locationTextChanced(s.toString())
                }
            })

            resultsRecyclerView.layoutManager = LinearLayoutManager(context)
            resultsRecyclerView.adapter = listAdapter

            val categoryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, categoryList)
            categorySpinner.adapter = categoryAdapter

            autoDetectCheckBox.setOnCheckedChangeListener { _, b ->

                if (b) {
                    getLoc()
                    viewModel.fetchDeviceLocation()
                    locationTextField.hide()
                } else {
                    viewModel.autoDetectLocationDisabled()
                    locationTextField.show()
                }
            }

            categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    categoryMap[categoryList[p2]]?.let { viewModel.updateCategory(it) }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }

            submitButton.setOnClickListener {
                if (keyWordTextField.text.isEmpty()) {
                    keyWordTextField.error = "This field is required"
                } else if (distanceTextField.text.isEmpty()) {
                    distanceTextField.error = "This field is required"
                } else if (locationTextField.text.isEmpty() && !autoDetectCheckBox.isChecked) {
                    locationTextField.error = "This field is required"
                } else {
                    viewModel.submitClicked(keyWordTextField.text.toString(), distanceTextField.text.toString())
                }
            }

        }

    }

    private fun getLoc() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation.addOnCompleteListener { task ->
            if (task.isSuccessful && task.result != null) {
                viewModel.updateLatLong(task.result.latitude, task.result.longitude)
            }

        }
    }
}