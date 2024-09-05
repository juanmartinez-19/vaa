package com.example.vanalaeropuerto.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vanalaeropuerto.data.ViewState
import java.util.Calendar

class HomeViewModel : ViewModel() {

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> get() = _viewState

    init {
        _viewState.value = ViewState.Idle
    }
    fun validarDatos(originAddress: String?, destinationAddress: String, luggage: Float?, passangers: Int?, selectedDateInMillis: Long?) {
        _viewState.value = ViewState.Loading

        val currentDateInMillis = Calendar.getInstance().timeInMillis
        val errores = mutableListOf<String>()

        if (luggage == null) {
            errores.add("Equipaje no puede ser nulo")
        } else if (luggage <= 0 || luggage > 100) {
            errores.add("Equipaje debe estar entre 1 y 100 kg")
        }

        if (passangers == null) {
            errores.add("Pasajeros no puede ser nulo")
        } else if (passangers <= 0 || passangers > 10) {
            errores.add("Número de pasajeros debe estar entre 1 y 10")
        }

        if (originAddress.isNullOrBlank()) {
            errores.add("Dirección de origen no puede estar vacía")
        }

        if (destinationAddress.isBlank()) {
            errores.add("Dirección de destino no puede estar vacía")
        }

        if (selectedDateInMillis==null) {
            errores.add("Fecha de salida no puede estar vacía")
        } else if (selectedDateInMillis <= currentDateInMillis) {
            errores.add("Fecha de salida debe ser mayor a la fecha actual")
        }

        if (errores.isNotEmpty()) {
            _viewState.value = ViewState.InvalidParameters
            Log.e("ValidarDatos", "Errores: ${errores.joinToString(", ")}")
        } else {
            _viewState.value = ViewState.Idle
        }
    }




}