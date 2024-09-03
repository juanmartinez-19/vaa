package com.example.vanalaeropuerto.viewmodels

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
    fun validarDatos (direccionOrigen: String?, direccionDestino: String?,equipaje: Int?, pasajeros: Int?) {
        _viewState.value = ViewState.Loading

       if (equipaje == null) {
            _viewState.value = ViewState.InvalidParameters
       } else
       if (pasajeros == null) {
            _viewState.value = ViewState.InvalidParameters
       } else
       if (direccionOrigen.isNullOrBlank()) {
            _viewState.value = ViewState.InvalidParameters
       } else
       if (direccionDestino.isNullOrBlank()) {
             _viewState.value = ViewState.InvalidParameters
       } else
       if (equipaje <= 0) {
            _viewState.value = ViewState.InvalidParameters
       } else
       if (equipaje > 100) {
            _viewState.value = ViewState.InvalidParameters
       } else
       if (pasajeros <= 0) {
            _viewState.value = ViewState.InvalidParameters
       } else
       if (pasajeros > 10) {
            _viewState.value = ViewState.InvalidParameters
       } else
       {
            _viewState.value = ViewState.Idle
       }
    }


}