package com.example.vanalaeropuerto.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vanalaeropuerto.data.ViewState
import java.util.Calendar

class IngresoDatosViewModel : ViewModel() {

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> get() = _viewState

    fun validateUserData(
        radiobuttonChecked: Boolean,
        name: String?,
        surname: String?,
        phoneNumber: String?,
        cuil: String?
    ) {
        _viewState.value = ViewState.Loading

        val currentDateInMillis = Calendar.getInstance().timeInMillis
        val errores = mutableListOf<String>()

        // Validación de radiobutton
        if (!radiobuttonChecked) {
            errores.add("Debe seleccionar para quién corresponde el viaje")
        }

        // Validación de nombre
        if (name.isNullOrBlank()) {
            errores.add("Nombre no puede estar vacío")
        } else if (!name.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{2,50}\$"))) {
            errores.add("Nombre no es válido. Solo se permiten letras y debe tener entre 2 y 50 caracteres.")
        }

        // Validación de apellido
        if (surname.isNullOrBlank()) {
            errores.add("Apellido no puede estar vacío")
        } else if (!surname.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{2,50}\$"))) {
            errores.add("Apellido no es válido. Solo se permiten letras y debe tener entre 2 y 50 caracteres.")
        }

        // Validación de telefono
        if (phoneNumber.isNullOrBlank()) {
            errores.add("Telefono no puede estar vacía")
        } else if (!phoneNumber.matches(Regex("^[+]?[0-9]{10,13}\$"))) {
            errores.add("Teléfono no es válido. Debe contener entre 10 y 13 dígitos, y puede incluir un '+' al inicio.")
        }

        // Validación de cuil
        if (cuil.isNullOrBlank()) {
            errores.add("Cuil no puede estar vacía")
        } else if (!cuil.matches(Regex("^\\d{2}\\d{8}\\d{1}\$"))) {
            errores.add("CUIL no es válido. Debe tener el formato XX-XXXXXXXX-X.")
        }


        // Manejo de errores
        if (errores.isNotEmpty()) {
            _viewState.value = ViewState.InvalidParameters
            Log.e("ValidarDatos", "Errores: ${errores.joinToString(", ")}")
        } else {
            _viewState.value = ViewState.Confirmed
        }
    }
}