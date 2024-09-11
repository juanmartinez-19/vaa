package com.example.vanalaeropuerto.fragments.user

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.adapters.user.VehicleAdapter
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.viewmodels.user.VehiculosViewModel

class VehiculosFragment : Fragment() {

    lateinit var v : View
    lateinit var recyclerVehicles : RecyclerView
    lateinit var vehicleAdapter : VehicleAdapter
    // State
    lateinit var progressBar : ProgressBar

    private var luggage : Float = 0.0f
    private var passangers : Int = 0

    private lateinit var viewModel: VehiculosViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_vehiculos, container, false)

        recyclerVehicles = v.findViewById(R.id.recyclerVehicles)
        progressBar = v.findViewById(R.id.progressBarLoading)
        luggage = VehiculosFragmentArgs.fromBundle(requireArguments()).luggage
        passangers = VehiculosFragmentArgs.fromBundle(requireArguments()).passangers

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(VehiculosViewModel::class.java)

        recyclerVehicles.layoutManager = LinearLayoutManager(context)
        vehicleAdapter = VehicleAdapter(mutableListOf()) {
            val vehicle = vehicleAdapter.getSelectedProduct(it)
            val action = VehiculosFragmentDirections.actionVehiculosFragmentToIngresoDatosFragment()
            findNavController().navigate(action)
        }
        recyclerVehicles.adapter = vehicleAdapter

        viewModel.getProducts(passangers,luggage)

        viewModel._vehiclesList.observe(viewLifecycleOwner, Observer { _vehiclesList ->
            if (_vehiclesList != null) {
                vehicleAdapter.submitList(_vehiclesList)
            }
        })

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