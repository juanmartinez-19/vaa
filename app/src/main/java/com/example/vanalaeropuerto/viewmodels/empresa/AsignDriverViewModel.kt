package com.example.vanalaeropuerto.viewmodels.empresa

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vanalaeropuerto.data.MyResult
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.data.empresa.DriversRepository
import com.example.vanalaeropuerto.data.user.VehiclesRepository
import com.example.vanalaeropuerto.entities.Driver
import kotlinx.coroutines.launch

class AsignDriverViewModel : ViewModel() {
    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> get() = _viewState
    var _driversList : MutableLiveData<MutableList<Driver>?> = MutableLiveData()
    val getDriversUseCase : DriversRepository = DriversRepository()


    fun getDrivers(tripId : String) {
        viewModelScope.launch {
            _viewState.value = ViewState.Loading
            when (val result = getDriversUseCase.getDrivers(tripId)){
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