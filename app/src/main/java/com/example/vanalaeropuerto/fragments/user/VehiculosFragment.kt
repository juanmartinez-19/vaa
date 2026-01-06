package com.example.vanalaeropuerto.fragments.user

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.adapters.user.VehicleAdapter
import com.example.vanalaeropuerto.data.MyResult
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.data.login.RequesterRepository
import com.example.vanalaeropuerto.entities.Requester
import com.example.vanalaeropuerto.entities.Trip
import com.example.vanalaeropuerto.entities.TripRequester
import com.example.vanalaeropuerto.viewmodels.login.AuthViewModel
import com.example.vanalaeropuerto.viewmodels.user.UserSharedViewModel
import com.example.vanalaeropuerto.viewmodels.user.VehiculosViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.util.UUID

class VehiculosFragment : Fragment() {

    lateinit var v : View

    lateinit var recyclerVehicles : RecyclerView
    lateinit var vehicleAdapter : VehicleAdapter
    private val userViewModel: UserSharedViewModel by activityViewModels()
    private var currentRequester: Requester? = null

    // State
    lateinit var progressBar : ProgressBar

    private var luggage : Float = 0.0f
    private var adults : Int = 0
    private var children : Int = 0
    private var babies : Int = 0
    private var passangers : Int = 0
    private var originAddress: String?=""
    private var destinationAddress: String?=""
    private var departureDate: String?=""

    private lateinit var trip : Trip
    private lateinit var tripRequester : TripRequester

    private lateinit var viewModel: VehiculosViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_vehiculos, container, false)

        recyclerVehicles = v.findViewById(R.id.recyclerVehicles)
        progressBar = v.findViewById(R.id.progressBarLoading)
        luggage = VehiculosFragmentArgs.fromBundle(requireArguments()).luggage
        adults = VehiculosFragmentArgs.fromBundle(requireArguments()).adults
        children = VehiculosFragmentArgs.fromBundle(requireArguments()).children
        babies = VehiculosFragmentArgs.fromBundle(requireArguments()).babies
        originAddress = VehiculosFragmentArgs.fromBundle(requireArguments()).direccionOrigen
        destinationAddress = VehiculosFragmentArgs.fromBundle(requireArguments()).direccionDestino
        departureDate = VehiculosFragmentArgs.fromBundle(requireArguments()).fechaSalida

        passangers = adults + children + babies

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(VehiculosViewModel::class.java)

        recyclerVehicles.layoutManager = LinearLayoutManager(context)
        vehicleAdapter = VehicleAdapter(mutableListOf()) {
            val vehicle = vehicleAdapter.getSelectedProduct(it)
            val tripId = UUID.randomUUID().toString()

            trip = Trip(departureDate,originAddress,destinationAddress, adults, children, babies, luggage, vehicle.getVehiclePrice(), "PENDING",tripId,vehicle.getVehicleId(), currentRequester!!.getRequesterId())

            tripRequester = TripRequester(trip,currentRequester)
            val action = VehiculosFragmentDirections.actionVehiculosFragmentToIngresoDatosFragment(tripRequester)
            findNavController().navigate(action)
        }
        recyclerVehicles.adapter = vehicleAdapter

        viewModel.getVehicles(passangers,luggage)

        viewModel._vehiclesList.observe(viewLifecycleOwner, Observer { _vehiclesList ->
            if (_vehiclesList != null) {
                vehicleAdapter.submitList(_vehiclesList)
            }
        })

        userViewModel.requester.observe(viewLifecycleOwner) { requester ->
            if (requester == null) return@observe
            currentRequester = requester
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


    }

    private fun showEmpty() {
        progressBar.visibility = View.GONE
        recyclerVehicles.visibility = View.GONE
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        recyclerVehicles.visibility = View.GONE
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
        recyclerVehicles.visibility = View.VISIBLE
    }

    private fun showError() {
        progressBar.visibility = View.GONE
        recyclerVehicles.visibility = View.GONE
    }

}