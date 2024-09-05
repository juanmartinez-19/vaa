package com.example.vanalaeropuerto.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.viewmodels.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import java.util.Calendar

class HomeFragment : Fragment() {

    //Pantalla
    private lateinit var v : View
    private lateinit var etOriginAddress : EditText
    private lateinit var etDestinationAddress : EditText
    private lateinit var etPassangers : EditText
    private lateinit var etLuggage : EditText
    private lateinit var etDepartureDate : EditText
    private lateinit var btnSearch : Button
    //State
    private lateinit var progressBar : ProgressBar
    private lateinit var textViewTitle : TextView

    private var originAddress: String?=""
    private var destinationAddress: String?=""
    private var luggage: Float = 0F
    private var passangers: Int = 0
    private var departureDate: String?=""
    private var selectedDateInMillis: Long = 0

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v= inflater.inflate(R.layout.fragment_home, container, false)

        etOriginAddress = v.findViewById(R.id.etDireccionOrigen)
        etDestinationAddress = v.findViewById(R.id.etDireccionDestino)
        etPassangers = v.findViewById(R.id.etPasajeros)
        etLuggage = v.findViewById(R.id.etEquipaje)
        btnSearch = v.findViewById(R.id.btnBuscar)
        progressBar = v.findViewById(R.id.progressBarLoading)
        textViewTitle = v.findViewById(R.id.tvTitle)
        etDepartureDate = v.findViewById(R.id.etDepartureDate)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        etDepartureDate.setOnClickListener {
            this.showDatePickerDialog()
        }

        btnSearch.setOnClickListener {

            originAddress = etOriginAddress.text?.toString()

            destinationAddress = etDestinationAddress.text?.toString()

            departureDate = etDepartureDate.text?.toString()

            val equipajeString = etLuggage.text?.toString()
            luggage = if (!equipajeString.isNullOrBlank()) {
                equipajeString.toFloat()
            } else {
                0F
            }

            val passangersString = etPassangers.text?.toString()
            passangers = if (!passangersString.isNullOrBlank()) {
                passangersString.toInt()
            } else {
                0
            }

            viewModel.validarDatos(originAddress, destinationAddress, luggage, passangers, selectedDateInMillis)

            this.observeState()
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
            etDepartureDate.setText(selectedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun observeState () {
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
                   try {
                        if (findNavController().currentDestination?.id == R.id.homeFragment) {
                            val action = HomeFragmentDirections.actionHomeFragmentToVehiculosFragment(passangers,luggage)
                            findNavController().navigate(action)
                        }
                    } catch (e: IllegalArgumentException) {
                        Log.e("HomeFragment", "Navigation action failed: ${e.message}")
                    }

                }
                is ViewState.InvalidParameters -> {
                    this.showInvalidParameters()
                }
                else ->{
                    this.showError()
                }
            }
        })
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        textViewTitle.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
        textViewTitle.visibility = View.VISIBLE
    }

    private fun showError() {
        progressBar.visibility = View.GONE
        Snackbar.make(v, getString(R.string.ha_ocurrido_un_error), Snackbar.LENGTH_SHORT).show()
        textViewTitle.visibility = View.VISIBLE
    }

    private fun showInvalidParameters() {

        progressBar.visibility = View.GONE
        Snackbar.make(v, getString(R.string.invalid_parameters), Snackbar.LENGTH_SHORT).show()
        textViewTitle.visibility = View.VISIBLE
    }

}