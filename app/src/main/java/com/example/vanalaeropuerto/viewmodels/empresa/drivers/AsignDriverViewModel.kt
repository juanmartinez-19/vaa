package com.example.vanalaeropuerto.viewmodels.empresa.drivers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vanalaeropuerto.data.MyResult
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.data.repositories.TripsRepository
import com.example.vanalaeropuerto.data.repositories.empresa.DriversRepository
import com.example.vanalaeropuerto.entities.Driver
import com.example.vanalaeropuerto.entities.Trip
import kotlinx.coroutines.launch

class AsignDriverViewModel : ViewModel() {
    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> get() = _viewState
    var _driversList : MutableLiveData<MutableList<Driver>?> = MutableLiveData()
    val getDriversUseCase : DriversRepository = DriversRepository()
    val getTripsUseCase : TripsRepository = TripsRepository()
    private val _trip = MutableLiveData<Trip?>()
    val trip: LiveData<Trip?> get() = _trip

    fun asignDriverToTrip(tripId: String, driverId : String) {
        _viewState.value = ViewState.Loading
        viewModelScope.launch {
            when (val result = getTripsUseCase.assignDriverToTrip(tripId, driverId)){
                is MyResult.Success -> {
                    _trip.value = result.data
                    _viewState.value = ViewState.Idle
                }
                is MyResult.Failure -> {
                    _viewState.value = ViewState.Failure
                    Log.d("TEST", _viewState.value.toString())
                }
            }
        }
    }

    fun getDrivers() {
        _viewState.value = ViewState.Loading
        viewModelScope.launch {
            when (val result = getDriversUseCase.getDrivers()){
                is MyResult.Success -> {
                    if (result.data.isNotEmpty()) {
                        _driversList.value = result.data
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