package com.example.vanalaeropuerto.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vanalaeropuerto.data.ViewState

class HomeViewModel : ViewModel() {

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> get() = _viewState

    init {
        _viewState.value = ViewState.Idle
    }
    fun validarDatos(direccionOrigen: String?, direccionDestino: String?, equipaje: Int?, pasajeros: Int?) {
        _viewState.value = ViewState.Loading

        val errores = mutableListOf<String>()

        if (equipaje == null) {
            errores.add("Equipaje no puede ser nulo")
        } else if (equipaje <= 0 || equipaje > 100) {
            errores.add("Equipaje debe estar entre 1 y 100 kg")
        }

        if (pasajeros == null) {
            errores.add("Pasajeros no puede ser nulo")
        } else if (pasajeros <= 0 || pasajeros > 10) {
            errores.add("Número de pasajeros debe estar entre 1 y 10")
        }

        if (direccionOrigen.isNullOrBlank()) {
            errores.add("Dirección de origen no puede estar vacía")
        }

        if (direccionDestino.isNullOrBlank()) {
            errores.add("Dirección de destino no puede estar vacía")
        }

        if (errores.isNotEmpty()) {
            _viewState.value = ViewState.InvalidParameters
            Log.e("ValidarDatos", "Errores: ${errores.joinToString(", ")}")
        } else {
            _viewState.value = ViewState.Idle
        }
    }



}