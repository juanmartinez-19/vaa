package com.example.vanalaeropuerto.fragments.empresa

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RadioGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.vanalaeropuerto.R
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.viewmodels.empresa.CrudDriverViewModel
import com.example.vanalaeropuerto.viewmodels.empresa.DriversViewModel
import com.example.vanalaeropuerto.viewmodels.user.IngresoDatosViewModel
import com.google.android.material.snackbar.Snackbar
import java.util.UUID

class CrudDriverFragment : Fragment() {

    private lateinit var v : View

    private lateinit var progressBar : ProgressBar

    private lateinit var editTextNombre: EditText
    private lateinit var editTextApellido: EditText
    private lateinit var radioGroupButaca: RadioGroup
    private lateinit var editTextTelefono: EditText
    private lateinit var editTextCuil: EditText
    private lateinit var buttonGuardar: Button

    private lateinit var name: String
    private lateinit var surname : String
    private lateinit var phoneNumber: String
    private lateinit var cuil: String
    private var tieneButaca: Boolean = false

    private lateinit var driverId : String

    private lateinit var viewModel: CrudDriverViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_crud_driver, container, false)

        // Levantar variables de las vistas
        progressBar = v.findViewById(R.id.progressBarLoading)
        editTextNombre = v.findViewById(R.id.editTextNombre)
        editTextApellido = v.findViewById(R.id.editTextApellido)
        radioGroupButaca = v.findViewById(R.id.radioGroupButaca)
        editTextTelefono = v.findViewById(R.id.editTextTelefono)
        editTextCuil = v.findViewById(R.id.editTextCuil)
        buttonGuardar = v.findViewById(R.id.buttonGuardar)

        driverId = CrudDriverFragmentArgs.fromBundle(requireArguments()).driverId

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(CrudDriverViewModel::class.java)

        if (driverId.isNotBlank()) {
            viewModel.getDriverById(driverId)
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
                is ViewState.Confirmed -> {
                    this.hideLoading()
                }
                is ViewState.InvalidParameters -> {
                    this.showInvalidParameters(viewState.message)
                }
                else -> {
                    this.showError()
                }
            }
        })

        viewModel.driver.observe(viewLifecycleOwner, Observer { driver ->
            if (driver != null) {
                val driverName = driver.getDriverName().toString()
                val driverSurname = driver.getDriverSurname().toString()
                val driverTieneButaca = driver.getTieneButaca().toString().toBoolean()
                val driverTelefono = driver.getDriverPhoneNumber().toString()
                val driverCuil = driver.getDriverCuil().toString()


                if (driverTieneButaca) {
                    radioGroupButaca.check(R.id.radioSi)
                } else {
                    radioGroupButaca.check(R.id.radioNo)
                }

                editTextNombre.setText(driverName)
                editTextApellido.setText(driverSurname)
                editTextTelefono.setText(driverTelefono)
                editTextCuil.setText(driverCuil)
            }
        })
        // Acción del botón guardar
        buttonGuardar.setOnClickListener {
            name = editTextNombre.text.toString()
            surname = editTextApellido.text.toString()
            tieneButaca = when (radioGroupButaca.checkedRadioButtonId) {
                R.id.radioSi -> true
                else -> false
            }
            phoneNumber = editTextTelefono.text.toString()
            cuil = editTextCuil.text.toString()
            val driverId = UUID.randomUUID().toString()

            viewModel.addDriver(driverId, name,surname, tieneButaca,phoneNumber,cuil)
        }

    }



    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    private fun showError() {
        progressBar.visibility = View.GONE
        Snackbar.make(v, getString(R.string.ha_ocurrido_un_error), Snackbar.LENGTH_SHORT).show()
    }
    private fun showInvalidParameters(message : String) {
        progressBar.visibility = View.GONE
        Snackbar.make(v, message, Snackbar.LENGTH_SHORT).show()
    }

}