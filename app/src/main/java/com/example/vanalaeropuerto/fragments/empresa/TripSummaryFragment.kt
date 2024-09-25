package com.example.vanalaeropuerto.fragments.empresa

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.viewmodels.empresa.TripSummaryViewModel
import com.google.android.material.snackbar.Snackbar

class TripSummaryFragment : Fragment() {


    private lateinit var v: View
    private lateinit var progressBar : ProgressBar

    // Variables para los TextView
    private lateinit var tvRequesterName: TextView
    private lateinit var tvTripDate: TextView
    private lateinit var tvRequesterPhone: TextView
    private lateinit var tvRequesterCUIL: TextView
    private lateinit var tvOriginAddress: TextView
    private lateinit var tvDestinationAddress: TextView
    private lateinit var tvAdultCount: TextView
    private lateinit var tvChildCount: TextView
    private lateinit var tvBabyCount: TextView
    private lateinit var tvLuggage: TextView
    private lateinit var tvPrice: TextView
    private lateinit var tvDriverName : TextView

    private lateinit var btnConfirmTrip: Button

    private lateinit var viewModel: TripSummaryViewModel

    private lateinit var tripId : String
    private lateinit var requesterId : String
    private lateinit var driverId : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_trip_summary, container, false)


        progressBar = v.findViewById(R.id.progressBarLoading)
        tvRequesterName = v.findViewById(R.id.tvRequesterName)
        tvTripDate = v.findViewById(R.id.tvTripDate)
        tvRequesterPhone = v.findViewById(R.id.tvRequesterPhone)
        tvRequesterCUIL = v.findViewById(R.id.tvRequesterCUIL)
        tvOriginAddress = v.findViewById(R.id.tvOriginAddress)
        tvDestinationAddress = v.findViewById(R.id.tvDestinationAddress)
        tvAdultCount = v.findViewById(R.id.tvAdultCount)
        tvChildCount = v.findViewById(R.id.tvChildCount)
        tvBabyCount = v.findViewById(R.id.tvBabyCount)
        tvLuggage = v.findViewById(R.id.tvLuggage)
        tvPrice = v.findViewById(R.id.tvPrice)
        btnConfirmTrip = v.findViewById(R.id.btnConfirmTrip)
        tvDriverName = v.findViewById(R.id.tvDriverName)
        tripId = TripSummaryFragmentArgs.fromBundle(requireArguments()).tripId
        driverId = TripSummaryFragmentArgs.fromBundle(requireArguments()).driverId
        requesterId = TripSummaryFragmentArgs.fromBundle(requireArguments()).requesterId

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(TripSummaryViewModel::class.java)

        viewModel.getTrip(tripId)
        viewModel.getRequester(requesterId)
        viewModel.getDriver(driverId)

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

                else -> {
                    this.showError()
                }
            }
        })

        viewModel.driver.observe(viewLifecycleOwner, Observer { driver ->
            if (driver != null) {
                tvDriverName.text = driver.getDriverName() ?: ""
            }
        })

        viewModel.trip.observe(viewLifecycleOwner, Observer { trip ->

            if (trip != null) {
                tvTripDate.text = trip.getDate() ?: ""
                tvOriginAddress.text = trip.getOriginAddress() ?: ""
                tvDestinationAddress.text = trip.getDestinationAddress() ?: ""
                tvAdultCount.text = trip.getAdults()?.toString() ?: "0"
                tvChildCount.text = trip.getChildren()?.toString() ?: "0"
                tvBabyCount.text = trip.getBabies()?.toString() ?: "0"
                tvLuggage.text = trip.getLuggageKg()?.toString() ?: "0"
                tvPrice.text = trip.getPrice()?.toString() ?: "0.00"
            }

        })

        viewModel.requester.observe(viewLifecycleOwner, Observer { requester ->
            if (requester != null) {
                val fullName =
                    "${requester.getRequesterName() ?: ""} ${requester.getRequesterSurname() ?: ""}"
                tvRequesterName.text = fullName.trim()
                tvRequesterPhone.text = requester.getRequesterPhoneNumber() ?: ""
                tvRequesterCUIL.text = requester.getRequesterCuil() ?: ""
            }
        })

        btnConfirmTrip.setOnClickListener {
            viewModel.confirmTrip(tripId)

            try {
                if (findNavController().currentDestination?.id == R.id.tripSummaryFragment) {
                    val action = TripSummaryFragmentDirections.actionTripSummaryFragmentToHomeEmpresaFragment()
                    findNavController().navigate(action)
                }
            } catch (e: IllegalArgumentException) {
                Log.e("AsignDriverFragment", "Navigation action failed: ${e.message}")
            }
        }
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    private fun showError() {
        progressBar.visibility = View.GONE
        Snackbar.make(v, getString(R.string.ha_ocurrido_un_error), Snackbar.LENGTH_SHORT).show()
    }

}