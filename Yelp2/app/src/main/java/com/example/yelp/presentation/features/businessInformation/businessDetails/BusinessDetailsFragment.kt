package com.example.yelp.presentation.features.businessInformation.businessDetails

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yelp.R
import com.example.yelp.databinding.FragmentBusinessDetailsBinding
import com.example.yelp.domain.useCases.BusinessDetailsItem
import com.example.yelp.presentation.base.fragments.BaseFragment
import com.example.yelp.presentation.base.fragments.BinderFactory
import com.example.yelp.presentation.extensions.view.observe
import com.example.yelp.presentation.rvAdapter.ConfigurableAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class BusinessDetailsFragment: BaseFragment<FragmentBusinessDetailsBinding>() {

    override val viewModel: BusinessDetailsViewModel by viewModel()
    override val binderFactory: BinderFactory<FragmentBusinessDetailsBinding> =
        FragmentBusinessDetailsBinding::inflate

    private val adapter by lazy {
        ConfigurableAdapter()
            .addDelegateType(DetailsDelegate(::showReservationPopUp))
            .addDelegateType(ImagesDelegate())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //businessInformationViewModel.getBusinessDetails()

        viewLifecycleOwner.observe(viewModel.businessDetailsLiveData) {
            adapter.replace(it)
        }

        withBinder {
            detailsRecyclerView.layoutManager = LinearLayoutManager(context)
            detailsRecyclerView.adapter = adapter
        }

    }

    private fun showReservationPopUp(item: BusinessDetailsItem) {

        val builder = AlertDialog.Builder(requireContext())
            .create()
        val view = layoutInflater.inflate(R.layout.item_popup_reservation,null)
        val  submit = view.findViewById<Button>(R.id.submit_button)
        val cancelButton = view.findViewById<Button>(R.id.cancel_button)
        val emailEditText = view.findViewById<EditText>(R.id.emailEditText)
        val dateEditText = view.findViewById<EditText>(R.id.dateEditText)
        val timeEditText = view.findViewById<EditText>(R.id.timeEditText)
        val businessText = view.findViewById<TextView>(R.id.businessName)
        builder.setView(view)
        businessText.text = item.businessName
        dateEditText.setOnClickListener {
            // on below line we are getting
            // the instance of our calendar.
            val c = Calendar.getInstance()

            // on below line we are getting
            // our day, month and year.
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            // on below line we are creating a
            // variable for date picker dialog.
            val datePickerDialog = DatePickerDialog(
                // on below line we are passing context.
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    // on below line we are setting
                    // date to our edit text.
                    val dat = "${monthOfYear + 1}-$dayOfMonth-$year"
                    dateEditText.setText(dat)
                },
                // on below line we are passing year, month
                // and day for the selected date in our date picker.
                year,
                month,
                day
            )
            // at last we are calling show
            // to display our date picker dialog.
            datePickerDialog.show()
        }

        timeEditText.setOnClickListener {
            val c = Calendar.getInstance()

            // on below line we are getting
            // our day, month and year.
            val hour = c.get(Calendar.HOUR)
            val min = c.get(Calendar.MINUTE)
            val mTimePicker = TimePickerDialog(requireContext(),
                { _, hourOfDay, minute -> timeEditText.setText(String.format("%d : %d", hourOfDay, minute)) }, hour, min, false)
            mTimePicker.show()
        }

        cancelButton.setOnClickListener {
            builder.dismiss()
        }

        submit.setOnClickListener {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.text.toString()).matches()) {
                viewModel.displayToast("Invalid Email Address")
            } else if (!checkTimeValid(timeEditText.text.toString().filter { !it.isWhitespace() })){
                viewModel.displayToast("Time should be between 10AM and 5PM")
            } else {
                viewModel.saveReservation(emailEditText.text.toString(), dateEditText.text.toString(), timeEditText.text.toString())
                viewModel.displayToast("Reservation Booked")
            }
            builder.dismiss()
        }
        builder.setCanceledOnTouchOutside(true)
        builder.show()

    }

    private fun checkTimeValid(selectedTime: String): Boolean {
        var isValid = true
        try {
            val string1 = "10:00"
            val time1: Date = SimpleDateFormat("HH:mm").parse(string1)
            val calendar1 = Calendar.getInstance()
            calendar1.time = time1
            calendar1.add(Calendar.DATE, 1)
            val string2 = "17:00"
            val time2: Date = SimpleDateFormat("HH:mm").parse(string2)
            val calendar2 = Calendar.getInstance()
            calendar2.time = time2
            calendar2.add(Calendar.DATE, 1)

            val d: Date = SimpleDateFormat("HH:mm").parse(selectedTime)
            val calendar3 = Calendar.getInstance()
            calendar3.time = d
            calendar3.add(Calendar.DATE, 1)
            val x = calendar3.time
            isValid = x.after(calendar1.time) && x.before(calendar2.time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return isValid
    }


}