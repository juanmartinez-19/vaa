package com.example.vanalaeropuerto.fragments.empresa

import android.annotation.SuppressLint
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
import com.example.vanalaeropuerto.core.Roles
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.entities.TripRequester
import com.example.vanalaeropuerto.session.SessionViewModel
import com.example.vanalaeropuerto.viewmodels.empresa.EditTripViewModel
import com.google.android.material.snackbar.Snackbar
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

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

        val sessionViewModel =
            ViewModelProvider(requireActivity())[SessionViewModel::class.java]

        sessionViewModel.currentRequester.observe(viewLifecycleOwner) { session ->
            if (session == null || session.getRequesterRole() != Roles.ADMIN) {
                requireActivity().finish()
            }
        }

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
                    this.showInvalidParameters(viewState.message)
                }

                else -> {
                    this.showError()
                }
            }
        })

        viewModel.trip.observe(viewLifecycleOwner) { trip ->
            if (trip != null) {

                val dateMillis = trip.getDate()

                if (dateMillis != null && dateMillis > 0) {
                    selectedDateInMillis = dateMillis

                    departureDateEditText.setText(
                        SimpleDateFormat("d/M/yyyy", Locale.getDefault())
                            .format(Date(dateMillis))
                    )
                }

                originAddressEditText.setText(trip.getOriginAddress())
                destinationAddressEditText.setText(trip.getDestinationAddress())
                priceEditText.setText(trip.getPrice().toString())
            }
        }



        departureDateEditText.setOnClickListener {
            this.showDatePickerDialog()
        }

        viewModel.getTrip(pendingTripId)


        btnEdit.setOnClickListener {
            val originAddress = originAddressEditText.text.toString()
            val destinationAddress = destinationAddressEditText.text.toString()
            val priceString = priceEditText.text.toString()
            val price = priceString.toFloatOrNull()

            viewModel.updateTrip(pendingTripId,originAddress,destinationAddress,selectedDateInMillis,price)
        }

    }

    @SuppressLint("SetTextI18n")
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedCalendar = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month) // 0-based OK
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                selectedDateInMillis = selectedCalendar.timeInMillis

                departureDateEditText.setText(
                    "$dayOfMonth/${month + 1}/$year"
                )
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

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

    private fun showInvalidParameters(message: String) {

        progressBar.visibility = View.GONE
        Snackbar.make(v, message, Snackbar.LENGTH_SHORT).show()
    }

}