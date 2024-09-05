package com.example.vanalaeropuerto.fragments

import android.app.DatePickerDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ProgressBar
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
    private lateinit var etLuggage : EditText
    private lateinit var etDepartureDate : EditText
    private lateinit var btnSearch : Button
    private lateinit var btnAdultPlus : Button
    private lateinit var btnAdultMinus : Button
    private lateinit var btnChildMinus : Button
    private lateinit var btnChildPlus : Button
    private lateinit var btnBabyMinus : Button
    private lateinit var btnBabyPlus : Button
    private lateinit var tvAdultCount : TextView
    private lateinit var tvChildCount : TextView
    private lateinit var tvBabyCount : TextView


    //State
    private lateinit var progressBar : ProgressBar
    private lateinit var textViewTitle : TextView

    private var originAddress: String?=""
    private var destinationAddress: String?=""
    private var luggage: Float = 0F
    private var passengers: Int = 0
    private var departureDate: String?=""
    private var selectedDateInMillis: Long = 0

    private var adultCountString : String?=""
    private var adultCount : Int=0
    private var childCountString : String?=""
    private var childCount : Int=0
    private var babyCountString : String?=""
    private var babyCount : Int=0

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_home, container, false)

        etOriginAddress = v.findViewById(R.id.etDireccionOrigen)
        etDestinationAddress = v.findViewById(R.id.etDireccionDestino)
        etLuggage = v.findViewById(R.id.etEquipaje)
        btnSearch = v.findViewById(R.id.btnBuscar)
        progressBar = v.findViewById(R.id.progressBarLoading)
        textViewTitle = v.findViewById(R.id.tvTitle)
        etDepartureDate = v.findViewById(R.id.etDepartureDate)
        btnAdultPlus = v.findViewById(R.id.btnAdultPlus)
        btnAdultMinus = v.findViewById(R.id.btnAdultMinus)
        tvAdultCount = v.findViewById(R.id.tvAdultCount)

        btnChildPlus = v.findViewById(R.id.btnChildPlus)
        btnChildMinus = v.findViewById(R.id.btnChildMinus)
        tvChildCount = v.findViewById(R.id.tvChildCount)

        btnBabyPlus = v.findViewById(R.id.btnBabyPlus)
        btnBabyMinus = v.findViewById(R.id.btnBabyMinus)
        tvBabyCount = v.findViewById(R.id.tvBabyCount)

        adultCountString = tvAdultCount.text?.toString()

        adultCountString = tvAdultCount.text?.toString()
        adultCount = if (!adultCountString.isNullOrBlank()) {
            adultCountString!!.toInt()
        } else { 0 }

        childCountString = tvChildCount.text?.toString()
        childCount = if (!childCountString.isNullOrBlank()) {
            childCountString!!.toInt()
        } else { 0 }

        babyCountString = tvBabyCount.text?.toString()
        babyCount = if (!babyCountString.isNullOrBlank()) {
            babyCountString!!.toInt()
        } else { 0 }

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        viewModel.passengers.observe(viewLifecycleOwner) { count ->
            passengers = count
        }

        viewModel.adultCount.observe(viewLifecycleOwner) { count ->
            tvAdultCount.text = count.toString()
        }

        viewModel.childCount.observe(viewLifecycleOwner) { count ->
            tvChildCount.text = count.toString()
        }

        viewModel.babyCount.observe(viewLifecycleOwner) { count ->
            tvBabyCount.text = count.toString()
        }

        etDepartureDate.setOnClickListener {
            this.showDatePickerDialog()
        }

        btnAdultPlus.setOnClickListener {
            viewModel.addAdult()
        }

        btnAdultMinus.setOnClickListener {
            viewModel.removeAdult()
        }

        btnChildPlus.setOnClickListener {
            viewModel.addChild()
        }

        btnChildMinus.setOnClickListener {
            viewModel.removeChild()
        }

        btnBabyPlus.setOnClickListener {
            viewModel.addBaby()
        }

        btnBabyMinus.setOnClickListener {
            viewModel.removeBaby()
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

            viewModel.validarDatos(originAddress, destinationAddress, luggage, passengers, selectedDateInMillis)
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
                        this.navigate()
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
    }

    private fun navigate() {
        try {
            if (findNavController().currentDestination?.id == R.id.homeFragment) {
                val action = HomeFragmentDirections.actionHomeFragmentToVehiculosFragment(passengers,luggage)
                findNavController().navigate(action)
            }
        } catch (e: IllegalArgumentException) {
            Log.e("HomeFragment", "Navigation action failed: ${e.message}")
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