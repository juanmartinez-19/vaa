package com.example.vanalaeropuerto.fragments

import android.annotation.SuppressLint
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
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.viewmodels.HomeViewModel
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    //Pantalla
    private lateinit var v : View
    private lateinit var etDireccionOrigen : EditText
    private lateinit var spDireccionDestino : Spinner
    private lateinit var etPasajeros : EditText
    private lateinit var etEquipaje : EditText
    private lateinit var btnBuscar : Button
    private var direccionDestino: String=""
    //State
    private lateinit var progressBar : ProgressBar
    private lateinit var textViewTitle : TextView

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v= inflater.inflate(R.layout.fragment_home, container, false)

        etDireccionOrigen = v.findViewById(R.id.etDireccionOrigen)
        spDireccionDestino = v.findViewById(R.id.spDireccionDestino)
        etPasajeros = v.findViewById(R.id.etPasajeros)
        etEquipaje = v.findViewById(R.id.etEquipaje)
        btnBuscar = v.findViewById(R.id.btnBuscar)
        progressBar = v.findViewById(R.id.progressBarLoading)
        textViewTitle = v.findViewById(R.id.tvTitle)

        val opcionesDestino = listOf("Dirección destino", "Buenos Aires Aeroparque, Argentina (AEP)", "Buenos Aires Ezeiza, Argentina (EZE)")

        val adapter = object : ArrayAdapter<String>(requireContext(), R.layout.item_spinner, opcionesDestino) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val textView = view as TextView

                textView.setTextColor(
                    if (position == 0) android.graphics.Color.GRAY else android.graphics.Color.BLACK
                )

                return view
            }
        }

        spDireccionDestino.adapter = adapter

        return v
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)


        btnBuscar.setOnClickListener {

            val direccionOrigen = etDireccionOrigen.text?.toString()

            spDireccionDestino.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    if (position == 0) {
                        Snackbar.make(view, "Seleccione una dirección válida", Snackbar.LENGTH_SHORT).show()
                        return
                    }
                    val selectedOption = parent.getItemAtPosition(position).toString()
                    direccionDestino = selectedOption
                }
                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }

            val equipajeString = etEquipaje.text?.toString()
            val equipaje = if (!equipajeString.isNullOrBlank()) {
                equipajeString.toInt()
            } else {
                0
            }

            val pasajerosString = etPasajeros.text?.toString()
            val pasajeros = if (!pasajerosString.isNullOrBlank()) {
                pasajerosString.toInt()
            } else {
                0
            }
            println("holaholahola ${direccionDestino} aaa")
            viewModel.validarDatos(direccionOrigen, direccionDestino, equipaje, pasajeros)
            this.observeState()
        }
    }

    private fun observeState () {
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            when (viewState) {
                is ViewState.Loading -> {
                    this.showLoading()
                    println("culo1")
                }
                is ViewState.Failure -> {
                    this.showError()
                    println("culo2")
                }
                is ViewState.Idle -> {
                    println("culo3")
                    this.hideLoading()
                   try {
                        if (findNavController().currentDestination?.id == R.id.homeFragment) {
                            val action = HomeFragmentDirections.actionHomeFragmentToVehiculosFragment()
                            findNavController().navigate(action)
                        }
                    } catch (e: IllegalArgumentException) {
                        Log.e("HomeFragment", "Navigation action failed: ${e.message}")
                    }

                }
                is ViewState.Empty ->{
                    println("culo4")
                    this.showEmpty()
                }
                is ViewState.InvalidParameters -> {
                    println("culo5")
                    this.showInvalidParameters()
                }
                else ->{
                    println("culo6")
                    this.showError()
                }
            }
        })
    }

    private fun showEmpty() {
        progressBar.visibility = View.GONE
        Snackbar.make(v, "Seleccione una dirección válida", Snackbar.LENGTH_SHORT).show()
        textViewTitle.visibility = View.VISIBLE
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