package com.example.vanalaeropuerto.viewmodels.empresa

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vanalaeropuerto.data.MyResult
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.data.TripsRepository
import com.example.vanalaeropuerto.entities.Trip
import kotlinx.coroutines.launch
import java.util.Calendar

class EditTripViewModel : ViewModel() {

    private val errores = mutableListOf<String>()

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> get() = _viewState
    val getTripsUseCase : TripsRepository = TripsRepository()

    private val _trip = MutableLiveData<Trip?>()
    val trip: LiveData<Trip?> get() = _trip

    fun getTrip(tripId : String) {
        _viewState.value = ViewState.Loading

        viewModelScope.launch {
            when (val result = getTripsUseCase.getTrip(tripId)) {
                is MyResult.Success -> {
                    _trip.value = result.data
                    _viewState.value = ViewState.Idle
                }

                is MyResult.Failure -> {
                    _trip.value = null
                    _viewState.value = ViewState.Failure
                    Log.d("TEST", _viewState.value.toString())
                }
            }
        }
    }

    fun validarDatos(
        originAddress: String?,
        destinationAddress: String?,
        selectedDateInMillis: Long?,
        price:Float?
    ) {
        val currentDateInMillis = Calendar.getInstance().timeInMillis

        if (price != null) {
            if (price <= 0) {
                errores.add("El precio no puede ser menor o igual a 0.")
            }
        } else {
            errores.add("El precio no puede estar vacío.")
        }

        // Validación de dirección de origen
        if (originAddress.isNullOrBlank()) {
            errores.add("Dirección de origen no puede estar vacía")
        }

        // Validación de dirección de destino
        if (destinationAddress.isNullOrBlank()) {
            errores.add("Dirección de destino no puede estar vacía")
        }

        // Validación de fecha
        if (selectedDateInMillis == null) {
            errores.add("Fecha de salida no puede estar vacía")
        } else if (selectedDateInMillis <= currentDateInMillis) {
            errores.add("Fecha de salida debe ser mayor a la fecha actual")
        }

    }

    fun updateTrip (
        tripId : String?,
        originAddress: String?,
        destinationAddress: String?,
        selectedDateInMillis: Long?,
        price:Float?
    ){
        _viewState.value = ViewState.Loading

        this.errores.clear()

        this.validarDatos(originAddress,destinationAddress,selectedDateInMillis,price)

        if (errores.isNotEmpty()) {
            _viewState.value = ViewState.InvalidParameters("")
            Log.e("ValidarDatos", "Errores: ${errores.joinToString(", ")}")
        } else {
            viewModelScope.launch {

                _viewState.value = ViewState.Loading

                when (val result = getTripsUseCase.updateTrip(tripId,originAddress,destinationAddress,selectedDateInMillis,price)) {
                    is MyResult.Success -> {
                        _trip.value = result.data
                        _viewState.value = ViewState.Confirmed
                    }
                    is MyResult.Failure -> {
                        _viewState.value = ViewState.Failure
                        Log.d("AddProductError", _viewState.value.toString())
                    }
                }

            }

        }


    }


}