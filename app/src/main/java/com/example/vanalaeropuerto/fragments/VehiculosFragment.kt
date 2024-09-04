package com.example.vanalaeropuerto.fragments

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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.adapters.VehicleAdapter
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.viewmodels.VehiculosViewModel

class VehiculosFragment : Fragment() {

    lateinit var v : View
    lateinit var recyclerVehicles : RecyclerView
    lateinit var vehicleAdapter : VehicleAdapter
    // State
    lateinit var progressBar : ProgressBar
    lateinit var textViewError : TextView

    companion object {
        fun newInstance() = VehiculosFragment()
    }

    private lateinit var viewModel: VehiculosViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_vehiculos, container, false)

        recyclerVehicles = v.findViewById(R.id.recyclerVehicles)
        progressBar = v.findViewById(R.id.progressBarLoading)

        val navController = findNavController()
        val backStackEntryCount = navController.currentBackStackEntry?.let {
            it.arguments?.size() ?: 0
        } ?: 0

        Log.d("Navigation", "Back stack entry count: $backStackEntryCount")




        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(VehiculosViewModel::class.java)

        recyclerVehicles.layoutManager = LinearLayoutManager(context)
        vehicleAdapter = VehicleAdapter(mutableListOf()) {
            val vehicle = vehicleAdapter.getSelectedProduct(it)
            //val action = HomeFragmentDirections.actionHomeFragmentToProductDetailFragment(product)
            //findNavController().navigate(action)
        }
        recyclerVehicles.adapter = vehicleAdapter

        viewModel.getProducts()

        this.observeProductsList()

        this.observeState()


    }

    private fun observeProductsList() {
        viewModel._vehiclesList.observe(viewLifecycleOwner, Observer { _productsList ->
            if (_productsList != null) {
                vehicleAdapter.submitList(_productsList)
            }
        })
    }

    private fun observeState() {
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