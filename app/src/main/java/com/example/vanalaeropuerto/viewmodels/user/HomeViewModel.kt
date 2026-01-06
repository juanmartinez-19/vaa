package com.example.vanalaeropuerto.viewmodels.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vanalaeropuerto.data.ViewState
import java.util.Calendar

class HomeViewModel : ViewModel() {

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> get() = _viewState

    private val _passengers = MutableLiveData(0)
    val passengers: LiveData<Int> get() = _passengers

    // LiveData para los contadores
    private val _adultCount = MutableLiveData(0)
    val adultCount: LiveData<Int> get() = _adultCount

    private val _childCount = MutableLiveData(0)
    val childCount: LiveData<Int> get() = _childCount

    private val _babyCount = MutableLiveData(0)
    val babyCount: LiveData<Int> get() = _babyCount


    init {
        _viewState.value = ViewState.Idle
    }

    fun addAdult() {
        _adultCount.value = (_adultCount.value ?: 0) + 1
        this.updateTotalPassengers()
    }

    fun removeAdult() {
        _adultCount.value = (_adultCount.value ?: 0).coerceAtLeast(1) - 1
        this.updateTotalPassengers()
    }

    fun addChild() {
        _childCount.value = (_childCount.value ?: 0) + 1
        this.updateTotalPassengers()
    }

    fun removeChild() {
        _childCount.value = (_childCount.value ?: 0).coerceAtLeast(1) - 1
        this.updateTotalPassengers()
    }

    fun addBaby() {
        _babyCount.value = (_babyCount.value ?: 0) + 1
        this.updateTotalPassengers()
    }

    fun removeBaby() {
        _babyCount.value = (_babyCount.value ?: 0).coerceAtLeast(1) - 1
        this.updateTotalPassengers()
    }

    private fun updateTotalPassengers() {
        val adults = _adultCount.value ?: 0
        val children = _childCount.value ?: 0
        val babies = _babyCount.value ?: 0

        _passengers.value = adults + children + babies
    }

    fun validarDatos(
        originAddress: String?,
        destinationAddress: String?,
        luggage: Float?,
        adults: Int?,
        children: Int?,
        babies: Int?,
        selectedDateInMillis: Long?,
        destinationAddresses: List<String>
    ) {
        _viewState.value = ViewState.Loading

        val currentDateInMillis = Calendar.getInstance().timeInMillis
        val errores = mutableListOf<String>()

        // Validación de fecha
        if (selectedDateInMillis == null) {
            errores.add("Fecha de salida no puede estar vacía")
        } else if (selectedDateInMillis <= currentDateInMillis) {
            errores.add("Fecha de salida debe ser mayor a la fecha actual")
        }

        // Validación de dirección de origen
        if (originAddress.isNullOrBlank()) {
            errores.add("Dirección de origen no puede estar vacía")
        }

        // Validación de dirección de destino
        if (destinationAddress.isNullOrBlank()) {
            errores.add("Dirección de destino no puede estar vacía")
        }

        // 🔴 NUEVO: Origen y destino no pueden ser iguales
        if (
            !originAddress.isNullOrBlank() &&
            !destinationAddress.isNullOrBlank() &&
            originAddress.trim().equals(destinationAddress.trim(), ignoreCase = true)
        ) {
            errores.add("La dirección de origen y destino no pueden ser iguales")
        }

        // Validación de direcciones de destino (si hay múltiples)
        if (destinationAddresses.any { it.isBlank() }) {
            errores.add("Todas las direcciones de destino deben estar completas")
        }

        // Validación de pasajeros
        val adultos = adults ?: 0
        val niños = children ?: 0
        val bebes = babies ?: 0

        val totalPasajeros = adultos + niños + bebes

        // mínimo 1 adulto
        if (adultos <= 0) {
            errores.add("Debe haber al menos un adulto")
        }

        if (totalPasajeros <= 0 || totalPasajeros > 10) {
            errores.add("Número de pasajeros debe estar entre 1 y 10")
        }

        // Validación de equipaje
        if (luggage == null) {
            errores.add("Equipaje no puede ser nulo")
        } else if (luggage <= 0 || luggage > 100) {
            errores.add("Equipaje debe estar entre 1 y 100 kg")
        }

        // Manejo de errores
        if (errores.isNotEmpty()) {
            _viewState.value = ViewState.InvalidParameters(errores.first())
            Log.e("ValidarDatos", "Errores: ${errores.joinToString(", ")}")
        } else {
            _viewState.value = ViewState.Idle
        }
    }






}