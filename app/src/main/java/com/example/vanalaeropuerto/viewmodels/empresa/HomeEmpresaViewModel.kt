package com.example.vanalaeropuerto.viewmodels.empresa

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vanalaeropuerto.data.MyResult
import com.example.vanalaeropuerto.data.user.VehiclesRepository
import com.example.vanalaeropuerto.data.ViewState
import com.example.vanalaeropuerto.data.empresa.TripsRepository
import com.example.vanalaeropuerto.entities.Trip
import kotlinx.coroutines.launch

class HomeEmpresaViewModel : ViewModel() {
    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> get() = _viewState
    var _tripsList : MutableLiveData<MutableList<Trip>?> = MutableLiveData()
    val getTripsUseCase : TripsRepository = TripsRepository()

    init {
        _viewState.value = ViewState.Idle
    }
    fun getTrips() {
        viewModelScope.launch {
            _viewState.value = ViewState.Loading
            when (val result = getTripsUseCase.getTrips()){
                is MyResult.Success -> {
                    if (result.data.isNotEmpty()) {
                        _tripsList.value = result.data
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