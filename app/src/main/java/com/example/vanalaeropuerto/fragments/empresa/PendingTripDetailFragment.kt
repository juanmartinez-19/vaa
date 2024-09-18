package com.example.vanalaeropuerto.fragments.empresa

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.entities.TripRequester
import com.example.vanalaeropuerto.fragments.user.IngresoDatosFragmentDirections
import com.example.vanalaeropuerto.fragments.user.VehiculosFragmentArgs.Companion.fromBundle
import com.example.vanalaeropuerto.viewmodels.empresa.ConfirmedTripsViewModel
import com.example.vanalaeropuerto.viewmodels.empresa.PendingTripDetailViewModel
import com.example.vanalaeropuerto.viewmodels.empresa.PendingTripsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PendingTripDetailFragment : Fragment() {

    private lateinit var v: View

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
    private lateinit var fabEditTrip : FloatingActionButton

    private lateinit var pendingTrip : TripRequester

    // Variables para los botones
    private lateinit var btnConfirmTrip: Button
    private lateinit var btnCancelTrip: Button

    private lateinit var viewModel: PendingTripDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_pending_trip_detail, container, false)

        // Asociar las vistas con las variables
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
        fabEditTrip = v.findViewById(R.id.fabEditTrip)

        arguments?.let {
            pendingTrip = it.getParcelable("tripRequester")!!
        }

        // Asociar los botones
        btnConfirmTrip = v.findViewById(R.id.btnConfirmTrip)
        btnCancelTrip = v.findViewById(R.id.btnCancelTrip)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(PendingTripDetailViewModel::class.java)

        // Asignar los valores de la clase TripRequester a los TextView
        pendingTrip.getTrip()?.let { trip ->
            tvTripDate.text = trip.getDate() ?: ""
            tvOriginAddress.text = trip.getOriginAddress() ?: ""
            tvDestinationAddress.text = trip.getDestinationAddress() ?: ""
            tvAdultCount.text = trip.getAdults()?.toString() ?: "0"
            tvChildCount.text = trip.getChildren()?.toString() ?: "0"
            tvBabyCount.text = trip.getBabies()?.toString() ?: "0"
            tvLuggage.text = trip.getLuggageKg()?.toString() ?: "0"
            tvPrice.text = trip.getPrice()?.toString() ?: "0.00"
        }

        pendingTrip.getRequester()?.let { requester ->
            val fullName = "${requester.getRequesterName() ?: ""} ${requester.getRequesterSurname() ?: ""}"
            tvRequesterName.text = fullName.trim()
            tvRequesterPhone.text = requester.getRequesterPhoneNumber() ?: ""
            tvRequesterCUIL.text = requester.getRequesterCuil() ?: ""
        }

        fabEditTrip.setOnClickListener{
            try {
                if (findNavController().currentDestination?.id == R.id.pendingTripDetailFragment) {
                    val action = PendingTripDetailFragmentDirections.actionPendingTripDetailFragmentToEditTripFragment(pendingTrip)
                    findNavController().navigate(action)
                }
            } catch (e: IllegalArgumentException) {
                Log.e("PendingTripDetailFragment", "Navigation action failed: ${e.message}")
            }
        }



    }


    }
