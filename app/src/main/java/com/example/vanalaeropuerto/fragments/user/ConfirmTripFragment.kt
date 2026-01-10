package com.example.vanalaeropuerto.fragments.user

import android.annotation.SuppressLint
import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.core.Roles
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.entities.TripRequester
import com.example.vanalaeropuerto.session.SessionViewModel
import com.example.vanalaeropuerto.viewmodels.user.ConfirmTripViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ConfirmTripFragment : Fragment() {

    lateinit var v : View

    // State
    lateinit var progressBar : ProgressBar

    private lateinit var viewModel: ConfirmTripViewModel

    private lateinit var btnConfirm : FloatingActionButton
    private lateinit var tvDepartureDate: TextView
    private lateinit var tvOrigin: TextView
    private lateinit var tvDestination: TextView
    private lateinit var tvPassengers: TextView
    private lateinit var tvLuggage: TextView
    private lateinit var tvVehicle: TextView
    private lateinit var tvPrice: TextView

    private lateinit var tripRequester : TripRequester

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_confirm_trip, container, false)

        btnConfirm = v.findViewById(R.id.btnConfirm)
        tvDepartureDate = v.findViewById(R.id.tvDepartureDate)
        tvOrigin = v.findViewById(R.id.tvOrigin)
        tvDestination = v.findViewById(R.id.tvDestination)
        tvPassengers = v.findViewById(R.id.tvPassengers)
        tvLuggage = v.findViewById(R.id.tvLuggage)
        tvVehicle = v.findViewById(R.id.tvVehicle)
        tvPrice = v.findViewById(R.id.tvPrice)
        progressBar = v.findViewById(R.id.progressBarLoading)


        tripRequester = IngresoDatosFragmentArgs.fromBundle(requireArguments()).tripRequester

        tvDepartureDate.text = tripRequester.getTrip()?.getDate().toString()
        tvOrigin.text = tripRequester.getTrip()?.getOriginAddress()
        tvDestination.text = tripRequester.getTrip()?.getDestinationAddress()
        tvPassengers.text = tripRequester.getTrip()?.getAdults().toString()
        tvLuggage.text = tripRequester.getTrip()?.getLuggageKg().toString()
        tvVehicle.text = "Toyota Corolla"//tripRequester.getTrip()?.getSegmentoId()
        tvPrice.text = tripRequester.getTrip()?.getPrice() ?.let { String.format("$%,d", it.toInt()) }

        tripRequester = IngresoDatosFragmentArgs.fromBundle(requireArguments()).tripRequester

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ConfirmTripViewModel::class.java)

        val sessionViewModel =
            ViewModelProvider(requireActivity())[SessionViewModel::class.java]

        sessionViewModel.currentRequester.observe(viewLifecycleOwner) { session ->
            if (session == null || session.getRequesterRole() != Roles.USER) {
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
                is ViewState.Empty ->{
                    this.showEmpty()
                } else ->{
                this.showError()
            }
            }
        })

        btnConfirm.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Solicitud confirmada")
            builder.setMessage("Su solicitud ha sido procesada correctamente. Un agente se comunicará con usted en breve.")
            builder.setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
                this.confirmTrip()
                this.navigate()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

    }

    private fun confirmTrip() {
      viewModel.addTrip(tripRequester.getTrip())
    }

    private fun navigate() {
        try {
            if (findNavController().currentDestination?.id == R.id.confirmTripFragment) {
                val action = ConfirmTripFragmentDirections.actionConfirmTripFragmentToHomeFragment()
                findNavController().navigate(action)
            }
        } catch (e: IllegalArgumentException) {
            Log.e("ConfirmTripFragment", "Navigation action failed: ${e.message}")
        }
    }
    private fun showEmpty() {
        progressBar.visibility = View.GONE
        }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    private fun showError() {
        progressBar.visibility = View.GONE
    }


}