package com.example.vanalaeropuerto.fragments.empresa

import android.app.DatePickerDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.entities.TripRequester
import com.example.vanalaeropuerto.viewmodels.empresa.EditTripViewModel
import com.google.android.material.snackbar.Snackbar
import java.time.format.DateTimeFormatter
import java.util.Calendar

class EditTripFragment : Fragment() {

    private lateinit var v : View
    private lateinit var progressBar : ProgressBar

    private lateinit var btnEdit: Button
    private lateinit var departureDateEditText: EditText
    private lateinit var originAddressEditText: EditText
    private lateinit var destinationAddressEditText: EditText
    private lateinit var priceEditText: EditText

    private lateinit var departureDate : String
    private lateinit var originAddress : String
    private lateinit var destinationAddress : String
    private var price : Float = 0.0f

    private var selectedDateInMillis: Long = 0
    private var tripId : String? = null

    private lateinit var pendingTripId : String

    private lateinit var viewModel: EditTripViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v=  inflater.inflate(R.layout.fragment_edit_trip, container, false)

        pendingTripId = EditTripFragmentArgs.fromBundle(requireArguments()).tripId
        departureDateEditText = v.findViewById(R.id.etDepartureDate)
        originAddressEditText = v.findViewById(R.id.etDireccionOrigen)
        destinationAddressEditText = v.findViewById(R.id.etDireccionDestino)
        priceEditText = v.findViewById(R.id.etPrice)
        btnEdit = v.findViewById(R.id.btnEdit)
        progressBar = v.findViewById(R.id.progressBarLoading)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(EditTripViewModel::class.java)

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            when (viewState) {
                is ViewState.Loading -> {
                    this.showLoading()
                }

                is ViewState.Failure -> {
                    this.showError()
                }

                is ViewState.Idle -> {
                    this.hideLoading()
                }

                is ViewState.Confirmed -> {
                    this.confirmed()
                }

                is ViewState.InvalidParameters -> {
                    this.showInvalidParameters()
                }

                else -> {
                    this.showError()
                }
            }
        })

        viewModel.trip.observe(viewLifecycleOwner, Observer { trip ->
            if (trip != null) {
                departureDate = trip.getDate().toString()
                originAddress = trip.getOriginAddress().toString()
                destinationAddress = trip.getDestinationAddress().toString()
                price = trip.getPrice().toString().toFloat()

                departureDateEditText.setText(departureDate)
                originAddressEditText.setText(originAddress)
                destinationAddressEditText.setText(destinationAddress)
                priceEditText.setText(price.toString())
            }
        })

        departureDateEditText.setOnClickListener {
            this.showDatePickerDialog()
        }

        viewModel.getTrip(pendingTripId)


        btnEdit.setOnClickListener {
            val originAddress = originAddressEditText.text.toString()
            val destinationAddress = destinationAddressEditText.text.toString()
            val priceString = priceEditText.text.toString()
            val price = priceString.toFloat()

            viewModel.updateTrip(tripId,originAddress,destinationAddress,selectedDateInMillis,price)
        }

    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.set(year, month, dayOfMonth)
            selectedDateInMillis = selectedCalendar.timeInMillis

            val selectedDate = "$dayOfMonth/${month + 1}/$year"
            departureDateEditText.setText(selectedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    private fun confirmed(){
        activity?.supportFragmentManager?.popBackStack()
    }

    private fun showError() {
        progressBar.visibility = View.GONE
        Snackbar.make(v, getString(R.string.ha_ocurrido_un_error), Snackbar.LENGTH_SHORT).show()
    }

    private fun showInvalidParameters() {

        progressBar.visibility = View.GONE
        Snackbar.make(v, getString(R.string.invalid_parameters), Snackbar.LENGTH_SHORT).show()
    }

}