package com.example.vanalaeropuerto.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vanalaeropuerto.data.MyResult
import com.example.vanalaeropuerto.data.VehiclesRepository
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.entities.Vehicle
import kotlinx.coroutines.launch

class VehiculosViewModel : ViewModel() {
    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> get() = _viewState
    var _vehiclesList : MutableLiveData<MutableList<Vehicle>?> = MutableLiveData()
    val getVehiclesUseCase : VehiclesRepository = VehiclesRepository()

    init {
        _viewState.value = ViewState.Idle
    }
    fun getProducts() {
        viewModelScope.launch {
            _viewState.value = ViewState.Loading
            when (val result = getVehiclesUseCase.getVehicles()){
                is MyResult.Success -> {
                    if (result.data.isNotEmpty()) {
                        _vehiclesList.value = result.data
                        _viewState.value = ViewState.Idle

                    } else {
                        _viewState.value = ViewState.Empty
                    }
                }
                is MyResult.Failure -> {
                    _viewState.value = ViewState.Failure
                    Log.d("TEST", _viewState.value.toString())
                }
            }


        }
    }


}