package com.example.vanalaeropuerto.fragments

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

class HomeFragment : Fragment() {

    //Pantalla
    private lateinit var v : View
    private lateinit var etOriginAddress : EditText
    private lateinit var spDestinationAddress : Spinner
    private lateinit var etPassangers : EditText
    private lateinit var etLuggage : EditText
    private lateinit var btnSearch : Button
    //State
    private lateinit var progressBar : ProgressBar
    private lateinit var textViewTitle : TextView

    private var originAddress: String?=""
    private var destinationAddress: String=""
    private var luggage: Float = 0F
    private var passangers: Int = 0

    val opcionesDestino = listOf("Dirección destino", "Buenos Aires Aeroparque, Argentina (AEP)", "Buenos Aires Ezeiza, Argentina (EZE)")

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v= inflater.inflate(R.layout.fragment_home, container, false)

        etOriginAddress = v.findViewById(R.id.etDireccionOrigen)
        spDestinationAddress = v.findViewById(R.id.spDireccionDestino)
        etPassangers = v.findViewById(R.id.etPasajeros)
        etLuggage = v.findViewById(R.id.etEquipaje)
        btnSearch = v.findViewById(R.id.btnBuscar)
        progressBar = v.findViewById(R.id.progressBarLoading)
        textViewTitle = v.findViewById(R.id.tvTitle)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        val adapter = object : ArrayAdapter<String>(requireContext(), R.layout.item_spinner, opcionesDestino) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val v = super.getDropDownView(position, convertView, parent)
                val textView = v as TextView

                textView.setTextColor(
                    if (position == 0) android.graphics.Color.GRAY else android.graphics.Color.BLACK
                )

                return v
            }
        }

        spDestinationAddress.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
               if (position == 0) {
                    return
                }
                val selectedOption = parent.getItemAtPosition(position).toString()
                destinationAddress = selectedOption
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }


        spDestinationAddress.adapter = adapter

        btnSearch.setOnClickListener {

            originAddress = etOriginAddress.text?.toString()

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

            viewModel.validarDatos(originAddress, destinationAddress, luggage, passangers)
            this.observeState()
        }
    }

    override fun onResume() {
        super.onResume()

        val selectedPosition = opcionesDestino.indexOf(destinationAddress)
        spDestinationAddress.setSelection(if (selectedPosition != -1) selectedPosition else 0)
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
                            val action = HomeFragmentDirections.actionHomeFragmentToVehiculosFragment(luggage, passangers)
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