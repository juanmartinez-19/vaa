package com.example.vanalaeropuerto.fragments.empresa.drivers

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
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
import com.example.vanalaeropuerto.adapters.empresa.DriverAdapter
import com.example.vanalaeropuerto.core.Roles
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.session.SessionViewModel
import com.example.vanalaeropuerto.viewmodels.empresa.drivers.AsignDriverViewModel

class AsignDriverFragment : Fragment() {

    private lateinit var v : View

    lateinit var driverAdapter : DriverAdapter
    private lateinit var recyclerDrivers : RecyclerView
    lateinit var progressBar : ProgressBar

    private lateinit var tripId : String
    private lateinit var requesterId : String

    private lateinit var viewModel: AsignDriverViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_asign_driver, container, false)

        recyclerDrivers = v.findViewById(R.id.recyclerDrivers)
        progressBar = v.findViewById(R.id.progressBarLoading)
        tripId = AsignDriverFragmentArgs.fromBundle(requireArguments()).tripId
        requesterId = AsignDriverFragmentArgs.fromBundle(requireArguments()).requesterId

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AsignDriverViewModel::class.java)

        val sessionViewModel =
            ViewModelProvider(requireActivity())[SessionViewModel::class.java]

        sessionViewModel.currentRequester.observe(viewLifecycleOwner) { session ->
            if (session == null || session.getRequesterRole() != Roles.ADMIN) {
                requireActivity().finish()
            }
        }

        recyclerDrivers.layoutManager = LinearLayoutManager(context)
        driverAdapter = DriverAdapter(mutableListOf()) {
            try {
                val driverId = driverAdapter.getSelectedProduct(it).getDriverId()

                if (!driverId.isNullOrBlank()) {
                    viewModel.asignDriverToTrip(tripId,driverId)
                }

                if (findNavController().currentDestination?.id == R.id.asignDriverFragment) {
                    if (!driverId.isNullOrBlank()) {
                        val action = AsignDriverFragmentDirections.actionAsignDriverFragmentToTripSummaryFragment(tripId,driverId,requesterId)
                        findNavController().navigate(action)
                    } else {
                        Log.e("AsignDriverFragment", "Navigation action failed")
                    }
                }
            } catch (e: IllegalArgumentException) {
                Log.e("AsignDriverFragment", "Navigation action failed: ${e.message}")
            }
        }
        recyclerDrivers.adapter = driverAdapter

        viewModel.getDrivers()

        viewModel._driversList.observe(viewLifecycleOwner, Observer { _driversList ->
            if (_driversList != null) {
                driverAdapter.submitList(_driversList)
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
        recyclerDrivers.visibility = View.GONE
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        recyclerDrivers.visibility = View.GONE
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
        recyclerDrivers.visibility = View.VISIBLE
    }

    private fun showError() {
        progressBar.visibility = View.GONE
        recyclerDrivers.visibility = View.GONE
    }

}